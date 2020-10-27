package wang.yeting.wtp.admin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.factory.WtpConfigFactory;
import wang.yeting.wtp.admin.factory.WtpFactory;
import wang.yeting.wtp.admin.service.WtpRegistryService;
import wang.yeting.wtp.admin.service.WtpService;
import wang.yeting.wtp.admin.thread.MainThreadPool;
import wang.yeting.wtp.admin.thread.PullConfigMonitorHandler;
import wang.yeting.wtp.admin.thread.WtpMonitorHandler;
import wang.yeting.wtp.admin.thread.WtpRegistryMonitorHandler;
import wang.yeting.wtp.admin.util.RedisUtils;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author : weipeng
 * @date : 2020-07-30 19:59
 */
@Slf4j
@SpringBootConfiguration
public class WtpHelperConfig implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {

    private ApplicationContext applicationContext;

    @Value("${config.refresh.second}")
    private Long configRefreshSecond;

    @Value("${registry.monitor.second}")
    private Long registryMonitorSecond;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        MainThreadPool.destroy();
        log.error("wtp ------> destroy.");
    }

    @Override
    public void afterSingletonsInstantiated() {
        initFactory();

        initMainThreadPool();

        registryMonitor();

        wtpMonitor();

        pullConfigMonitor();
    }

    private void initFactory(){
        RedisUtils redisUtils = applicationContext.getBean(RedisUtils.class);
        WtpFactory.refreshInstance();
        WtpFactory wtpFactory = WtpFactory.getInstance();
        WtpConfigFactory.refreshInstance(redisUtils,configRefreshSecond);
        WtpConfigFactory wtpConfigFactory = WtpConfigFactory.getInstance();

        WtpService wtpService = applicationContext.getBean(WtpService.class);
        List<Wtp> wtpList = wtpService.initConfigFactory();

        wtpFactory.loadWtp(wtpList);
        wtpConfigFactory.loadConfig(wtpList);
    }

    private void pullConfigMonitor() {
        try {
            MainThreadPool.execute(() -> {
                        PullConfigMonitorHandler pullConfigMonitorHandler = new PullConfigMonitorHandler();
                        pullConfigMonitorHandler.pullConfigMonitor();
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException("pullConfigMonitor");
        }
    }

    private void initMainThreadPool() {
        MainThreadPool.loadMainThreadPool(new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new SynchronousQueue<>(), r -> new Thread(r, "wtp main-" + r.hashCode()))
                , Executors.newCachedThreadPool()
                , new ScheduledThreadPoolExecutor(5, r -> new Thread(r, "wtp Scheduled-" + r.hashCode()))
        );
    }

    private void registryMonitor() {
        try {
            WtpRegistryService wtpRegistryService = (WtpRegistryService) applicationContext.getBean("wtpRegistryServiceImpl");
            WtpRegistryMonitorHandler wtpRegistryMonitorHandler = new WtpRegistryMonitorHandler(wtpRegistryService);
            wtpRegistryMonitorHandler.registryMonitor(registryMonitorSecond);
        } catch (Exception e) {
            throw new RuntimeException("registryMonitor");
        }
    }

    private void wtpMonitor() {
        try {
            WtpService wtpService = (WtpService) applicationContext.getBean("wtpServiceImpl");
            WtpMonitorHandler wtpMonitorHandler = new WtpMonitorHandler(wtpService);
            wtpMonitorHandler.wtpMonitor(configRefreshSecond);
        } catch (Exception e) {
            throw new RuntimeException("pushHealthLog");
        }
    }

}
