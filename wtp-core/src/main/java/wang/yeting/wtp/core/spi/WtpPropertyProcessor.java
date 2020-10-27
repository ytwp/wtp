package wang.yeting.wtp.core.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import wang.yeting.wtp.core.annotation.Wtp;
import wang.yeting.wtp.core.biz.client.AdminBiz;
import wang.yeting.wtp.core.biz.client.AdminBizClient;
import wang.yeting.wtp.core.biz.model.ConfigEvent;
import wang.yeting.wtp.core.biz.model.WtpBo;
import wang.yeting.wtp.core.biz.model.WtpConfigBean;
import wang.yeting.wtp.core.concurrent.ResizableCapacityLinkedBlockingQueue;
import wang.yeting.wtp.core.concurrent.WtpThreadPoolExecutor;
import wang.yeting.wtp.core.context.WtpAnnotationContext;
import wang.yeting.wtp.core.factory.WtpQueueFactory;
import wang.yeting.wtp.core.factory.WtpThreadPoolFactory;
import wang.yeting.wtp.core.handler.WtpHandler;
import wang.yeting.wtp.core.thread.ThreadPool;
import wang.yeting.wtp.core.thread.PullConfigHandler;
import wang.yeting.wtp.core.thread.PushLogHandler;
import wang.yeting.wtp.core.thread.TaskPullConfigHandler;
import wang.yeting.wtp.core.util.HttpResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : weipeng
 * @date : 2020-07-22 15:44
 */

@Slf4j
public class WtpPropertyProcessor implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    private ApplicationContext applicationContext;

    private WtpAnnotationContext wtpAnnotationContext;

    private WtpConfigBean wtpConfigBean;

    private static List<AdminBiz> adminBizList;

    public WtpPropertyProcessor(WtpConfigBean wtpConfigBean) {
        this.wtpConfigBean = wtpConfigBean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.wtpAnnotationContext = applicationContext.getBean(WtpAnnotationContext.class);
    }

    @Override
    public void destroy() {
        for (AdminBiz adminBiz : adminBizList) {
            adminBiz.destroy();
        }
        ThreadPool.destroy();
        WtpThreadPoolFactory.getInstance().destroy();
        log.error("wtp ------> destroy.");
    }

    @Override
    public void afterSingletonsInstantiated() {
        WtpQueueFactory.refreshInstance();

        WtpThreadPoolFactory.refreshInstance();

        start();
    }

    private void start() {
        log.warn("wtp ------> start.");

        //生成注册对象
        initAdminBizList();
        //注册到admin 并首次初始化线程池
        registryInAdmin();
        //放值WtpHandler
        initWtpHandler();
        //加载线程池
        initWtpThreadPool();
        //长轮询拉去新配置
        startPullConfig();
        //推送线程池日志
        pushLog();
        //定时去拉取全部配置
        taskPullConfig();

        log.warn("wtp ------> started.");
    }

    private void taskPullConfig() {
        ThreadPool.execute(() -> {
                    TaskPullConfigHandler taskPullConfigHandler = new TaskPullConfigHandler();
                    taskPullConfigHandler.taskPullConfig(adminBizList);
                }
        );
    }

    private void initWtpThreadPool() {
        int poolSize = 3 + adminBizList.size();
        ThreadPool.loadMainThreadPool(
                new ThreadPoolExecutor(poolSize, poolSize, 60, TimeUnit.SECONDS, new ResizableCapacityLinkedBlockingQueue<>(10), r -> new Thread(r, "wtp main-" + r.hashCode()))
                , new ScheduledThreadPoolExecutor(4, r -> new Thread(r, "wtp Scheduled-" + r.hashCode()))
        );
    }

    private void pushLog() {
        ThreadPool.execute(() -> {
                    PushLogHandler pushLogHandler = new PushLogHandler();
                    pushLogHandler.pushLog(adminBizList, wtpConfigBean);
                }
        );
    }

    private void startPullConfig() {
        ThreadPool.execute(() -> {
                    PullConfigHandler pullConfigHandler = new PullConfigHandler();
                    pullConfigHandler.pullConfig(adminBizList);
                }
        );
    }

    private void registryInAdmin() {
        ConfigEvent configEvent = null;
        for (AdminBiz adminBiz : adminBizList) {
            HttpResponse<ConfigEvent> response = adminBiz.registry();
            if (response.getStatusCode() == HttpResponse.SUCCESS_CODE) {
                configEvent = response.getBody();
            } else {
                log.error("wtp ------> register {} failed. ");
            }
        }
        if (configEvent == null) {
            throw new RuntimeException("wtp ------> All registries failed , adminBizList [" + adminBizList + "].");
        }
        initWtpQueueFactory(configEvent);
        initWtpThreadPoolFactory(configEvent);
    }

    private void initWtpQueueFactory(ConfigEvent configEvent) {
        WtpQueueFactory wtpQueueFactory = WtpQueueFactory.getInstance();
        wtpQueueFactory.initQueue(configEvent);
    }

    private void initWtpThreadPoolFactory(ConfigEvent configEvent) {
        WtpThreadPoolFactory wtpThreadPoolFactory = WtpThreadPoolFactory.getInstance();
        wtpThreadPoolFactory.injectService(configEvent);
    }

    private void initWtpHandler() {
        WtpThreadPoolFactory wtpThreadPoolFactory = WtpThreadPoolFactory.getInstance();
        Set<Map.Entry<String, List<WtpHandler>>> entrySet = wtpAnnotationContext.getWtpHandler().entrySet();
        for (Map.Entry<String, List<WtpHandler>> wtpHandlerEntry : entrySet) {
            String name = wtpHandlerEntry.getKey();
            WtpThreadPoolExecutor threadPool = wtpThreadPoolFactory.getThreadPool(name);
            List<WtpHandler> writeArrayList = wtpHandlerEntry.getValue();
            if (writeArrayList.isEmpty()) {
                continue;
            }
            if (threadPool == null) {
                WtpHandler wtpHandler = writeArrayList.get(0);
                Wtp wtp = wtpHandler.getWtp();
                threadPool = wtpThreadPoolFactory.loadDefault(wtp);
                registerNoConfigurationWtp(wtp);
                log.warn("wtp ------> {} No configuration Wtp.", wtp.value());
            }
            for (WtpHandler wtpHandler : writeArrayList) {
                wtpHandler.assignment(threadPool);
            }
        }
    }

    private void registerNoConfigurationWtp(Wtp wtp) {
        for (AdminBiz adminBiz : adminBizList) {
            WtpBo wtpBo = new WtpBo()
                    .setAppId(wtpConfigBean.getAppId())
                    .setClusterId(wtpConfigBean.getClusterId())
                    .setCorePoolSize(wtp.defaultCorePoolSize())
                    .setMaximumPoolSize(wtp.defaultMaximumPoolSize())
                    .setKeepAliveSeconds(wtp.defaultKeepAliveSeconds())
                    .setName(wtp.value())
                    .setQueueCapacity(wtp.defaultQueueCapacity())
                    .setQueueName(wtp.defaultQueueName().getQueueName())
                    .setRejectedExecutionHandlerName(wtp.rejectedExecutionHandlerName().getRejectedExecutionHandlerName());
            try {
                HttpResponse<Boolean> response = adminBiz.registerNoConfigurationWtp(wtpBo);
                if (response.getStatusCode() == HttpResponse.SUCCESS_CODE) {
                    return;
                }
            } catch (Exception e) {
                log.error("wtp ------> register NoConfiguration Wtp Exception {}. ", e);
            }
        }
        log.error("wtp ------> {} failed to register. ", wtp.value());
    }

    private void initAdminBizList() {
        if (adminBizList == null) {
            adminBizList = new ArrayList<>();
        }
        String adminAddresses = wtpConfigBean.getAdminUrls();
        if (adminAddresses != null && adminAddresses.trim().length() > 0) {
            for (String address : adminAddresses.trim().split(",")) {
                if (address != null && address.trim().length() > 0) {
                    AdminBiz adminBiz = new AdminBizClient(address.trim(), wtpConfigBean);
                    adminBizList.add(adminBiz);
                }
            }
        }
    }

}
