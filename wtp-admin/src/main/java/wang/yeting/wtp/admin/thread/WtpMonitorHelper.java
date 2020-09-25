package wang.yeting.wtp.admin.thread;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.factory.WtpConfigFactory;
import wang.yeting.wtp.admin.factory.WtpFactory;
import wang.yeting.wtp.admin.service.WtpService;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author : weipeng
 * @date : 2020-07-30 19:56
 */

@Slf4j
public class WtpMonitorHelper {

    private WtpService wtpService;

    private static ScheduledFuture<?> scheduledFuture;

    public WtpMonitorHelper(WtpService wtpService) {
        this.wtpService = wtpService;
    }

    public void wtpMonitor(Long configRefreshSecond) {
        scheduledFuture = MainThreadPool.getScheduledThreadPoolExecutor().scheduleAtFixedRate(() -> {
            try {
                doWtpMonitor();
            } catch (Exception e) {
                log.error("wtp ------> doWtpMonitor Exception = [{}]. ", e);
            }
        }, configRefreshSecond, configRefreshSecond, TimeUnit.SECONDS);
    }

    private void doWtpMonitor() {
        log.info("wtp ------> doWtpMonitor . ");
        List<Wtp> wtpList = wtpService.initConfigFactory();
        WtpFactory wtpFactory = WtpFactory.getInstance();
        wtpFactory.loadWtp(wtpList);
        WtpConfigFactory wtpConfigFactory = WtpConfigFactory.getInstance();
        wtpConfigFactory.loadConfig(wtpList);
    }

    public static void destroy() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }
}
