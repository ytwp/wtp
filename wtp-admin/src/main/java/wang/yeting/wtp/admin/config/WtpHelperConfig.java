package wang.yeting.wtp.admin.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import wang.yeting.wtp.admin.service.WtpRegistryService;
import wang.yeting.wtp.admin.service.WtpService;
import wang.yeting.wtp.admin.thread.MainThreadPool;
import wang.yeting.wtp.admin.thread.WtpMonitorHelper;
import wang.yeting.wtp.admin.thread.WtpRegistryMonitorHelper;
import wang.yeting.wtp.core.concurrent.ResizableCapacityLinkedBlockingQueue;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : weipeng
 * @date : 2020-07-30 19:59
 */
@SpringBootConfiguration
public class WtpHelperConfig implements ApplicationContextAware, SmartInitializingSingleton {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        initMainThreadPool();

        registryMonitor();

        wtpMonitor();
    }

    private void initMainThreadPool() {
        MainThreadPool.loadMainThreadPool(new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new ResizableCapacityLinkedBlockingQueue<>(10), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "wtp main-" + r.hashCode());
            }
        }));
    }

    private void registryMonitor() {
        try {
            MainThreadPool.execute(() -> {
                        WtpRegistryService wtpRegistryService = (WtpRegistryService) applicationContext.getBean("wtpRegistryServiceImpl");
                        WtpRegistryMonitorHelper wtpRegistryMonitorHelper = new WtpRegistryMonitorHelper(wtpRegistryService);
                        wtpRegistryMonitorHelper.registryMonitor();
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException("registryMonitor");
        }
    }

    private void wtpMonitor() {
        try {
            MainThreadPool.execute(() -> {
                        WtpService wtpService = (WtpService) applicationContext.getBean("wtpServiceImpl");
                        WtpMonitorHelper wtpMonitorHelper = new WtpMonitorHelper(wtpService);
                        wtpMonitorHelper.wtpMonitor();
                    }
            );
        } catch (Exception e) {
            throw new RuntimeException("pushHealthLog");
        }
    }
}
