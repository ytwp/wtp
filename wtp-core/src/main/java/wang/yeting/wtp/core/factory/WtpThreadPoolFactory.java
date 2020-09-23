package wang.yeting.wtp.core.factory;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.core.annotation.Wtp;
import wang.yeting.wtp.core.biz.model.Config;
import wang.yeting.wtp.core.biz.model.ConfigChangeEvent;
import wang.yeting.wtp.core.biz.model.ConfigEvent;
import wang.yeting.wtp.core.concurrent.WtpRejectedExecutionHandler;
import wang.yeting.wtp.core.concurrent.WtpThreadPoolExecutor;
import wang.yeting.wtp.core.enums.RejectedExecutionHandlerEnums;
import wang.yeting.wtp.core.exceptions.WtpConfigException;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author : weipeng
 * @date : 2020-07-23 23:03
 */
@Slf4j
public class WtpThreadPoolFactory {

    private ConcurrentMap<String, WtpThreadPoolExecutor> threadPoolExecutorConcurrentMap = new ConcurrentHashMap<>();

    private static WtpThreadPoolFactory wtpThreadPoolFactory = new WtpThreadPoolFactory();

    public static WtpThreadPoolFactory getInstance() {
        return wtpThreadPoolFactory;
    }

    public static void refreshInstance() {
        wtpThreadPoolFactory = new WtpThreadPoolFactory();
    }

    public WtpThreadPoolExecutor getThreadPool(String name) {
        return threadPoolExecutorConcurrentMap.get(name);
    }

    public ConcurrentMap<String, WtpThreadPoolExecutor> getThreadPoolConcurrentMap() {
        return threadPoolExecutorConcurrentMap;
    }

    public WtpThreadPoolExecutor loadDefault(Wtp wtp) {
        String name = wtp.value();
        Integer corePoolSize = wtp.defaultCorePoolSize();
        Integer maximumPoolSize = wtp.defaultMaximumPoolSize();
        Long keepAliveSeconds = wtp.defaultKeepAliveSeconds();
        WtpQueueFactory wtpQueueFactory = WtpQueueFactory.getInstance();
        BlockingQueue<Runnable> blockingQueue = wtpQueueFactory.loadDefaultQueue(wtp);
        String rejectedExecutionHandlerName = wtp.rejectedExecutionHandlerName().getRejectedExecutionHandlerName();
        WtpRejectedExecutionHandler rejectedExecutionHandler = RejectedExecutionHandlerEnums.getRejectedExecutionHandler(rejectedExecutionHandlerName);
        WtpThreadPoolExecutor threadPooExecutor = new WtpThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveSeconds, TimeUnit.SECONDS, blockingQueue, r -> new Thread(r, name + "-" + r.hashCode()), rejectedExecutionHandler);
        threadPoolExecutorConcurrentMap.put(name, threadPooExecutor);
        return threadPooExecutor;
    }


    /**
     * inject service of bean field
     *
     * @param configEvent
     */
    public void injectService(ConfigEvent configEvent) {
        WtpQueueFactory wtpQueueFactory = WtpQueueFactory.getInstance();
        Set<String> nameSet = configEvent.changedKeys();
        for (String name : nameSet) {
            try {
                Config change = configEvent.getChange(name);
                Integer corePoolSize = change.getCorePoolSize();
                Integer maximumPoolSize = change.getMaximumPoolSize();
                Long keepAliveSeconds = change.getKeepAliveSeconds();
                BlockingQueue<Runnable> blockingQueue = wtpQueueFactory.getQueue(name);
                String rejectedExecutionHandlerName = change.getRejectedExecutionHandlerName();
                WtpRejectedExecutionHandler rejectedExecutionHandler = RejectedExecutionHandlerEnums.getRejectedExecutionHandler(rejectedExecutionHandlerName);
                WtpThreadPoolExecutor threadPooExecutor = new WtpThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveSeconds, TimeUnit.SECONDS, blockingQueue, r -> new Thread(r, name + "-" + r.hashCode()), rejectedExecutionHandler);
                threadPoolExecutorConcurrentMap.put(name, threadPooExecutor);
            } catch (Exception e) {
                throw new WtpConfigException("wtp ------> Configuration error, please check configuration, name=[" + name + "]");
            }
        }
    }

    public void changeService(ConfigChangeEvent configChangeEvent) {
        Set<String> nameSet = configChangeEvent.changedKeys();
        for (String name : nameSet) {
            Config change = configChangeEvent.getChange(name);
            changeThreadPool(name, change);
            log.warn("wtp ------> change thread Pool , name : [" + name + "].");
        }
    }

    private void changeThreadPool(String name, Config config) {
        WtpThreadPoolExecutor threadPoolExecutor = threadPoolExecutorConcurrentMap.get(name);
        Integer corePoolSize = config.getCorePoolSize();
        if (Objects.nonNull(corePoolSize) && threadPoolExecutor.getCorePoolSize() != corePoolSize) {
            threadPoolExecutor.setCorePoolSize(corePoolSize);
        }
        Integer maximumPoolSize = config.getMaximumPoolSize();
        if (Objects.nonNull(maximumPoolSize) && threadPoolExecutor.getMaximumPoolSize() != maximumPoolSize) {
            threadPoolExecutor.setMaximumPoolSize(maximumPoolSize);
        }
        Long keepAliveSeconds = config.getKeepAliveSeconds();
        if (Objects.nonNull(keepAliveSeconds) && threadPoolExecutor.getKeepAliveTime(TimeUnit.SECONDS) != keepAliveSeconds) {
            threadPoolExecutor.setKeepAliveTime(keepAliveSeconds, TimeUnit.SECONDS);
        }
        Integer queueCapacity = config.getQueueCapacity();
        if (Objects.nonNull(queueCapacity)) {
            WtpQueueFactory wtpQueueFactory = WtpQueueFactory.getInstance();
            wtpQueueFactory.changeQueue(config);
        }
        String rejectedExecutionHandlerName = config.getRejectedExecutionHandlerName();
        if (Objects.nonNull(rejectedExecutionHandlerName)) {
            WtpRejectedExecutionHandler rejectedExecutionHandler = RejectedExecutionHandlerEnums.getRejectedExecutionHandler(rejectedExecutionHandlerName);
            WtpRejectedExecutionHandler wtpRejectedExecutionHandler = threadPoolExecutor.getWtpRejectedExecutionHandler();
            if (!rejectedExecutionHandler.getClass().equals(wtpRejectedExecutionHandler.getClass())) {
                threadPoolExecutor.setWtpRejectedExecutionHandler(rejectedExecutionHandler);
            }
        }
    }

    public void refreshConfig(ConfigEvent configEvent) {
        Set<String> nameSet = configEvent.changedKeys();
        for (String name : nameSet) {
            Config change = configEvent.getChange(name);
            changeThreadPool(name, change);
            log.warn("wtp ------> refresh thread Pool , name : [" + name + "].");
        }
    }

    public void destroy() {
        for (WtpThreadPoolExecutor wtpThreadPoolExecutor : threadPoolExecutorConcurrentMap.values()) {
            wtpThreadPoolExecutor.shutdown();
        }
    }
}
