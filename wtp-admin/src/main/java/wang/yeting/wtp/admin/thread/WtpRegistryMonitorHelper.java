package wang.yeting.wtp.admin.thread;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.admin.bean.WtpRegistry;
import wang.yeting.wtp.admin.service.WtpRegistryService;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author : weipeng
 * @date : 2020-07-30 19:56
 */

@Slf4j
public class WtpRegistryMonitorHelper {

    private WtpRegistryService wtpRegistryService;

    private static ScheduledFuture<?> scheduledFuture;

    public WtpRegistryMonitorHelper(WtpRegistryService wtpRegistryService) {
        this.wtpRegistryService = wtpRegistryService;
    }

    public void registryMonitor(Long registryMonitorSecond) {
        scheduledFuture = MainThreadPool.getScheduledThreadPoolExecutor().scheduleWithFixedDelay(() -> {
            try {
                doRegistryMonitor();
            } catch (Exception e) {
                log.error("wtp ------> doRegistryMonitor Exception = [{}]. ", e);
            }
        }, 0, registryMonitorSecond, TimeUnit.SECONDS);
    }

    private void doRegistryMonitor() {
        log.info("wtp ------> doRegistryMonitor . ");
        long now = System.currentTimeMillis();
        long lastPullConfigTime = now - 300000L;
        List<WtpRegistry> wtpRegistryList = wtpRegistryService.findByLastPullConfigTime(lastPullConfigTime);
        if (CollectionUtil.isNotEmpty(wtpRegistryList)) {
            for (WtpRegistry wtpRegistry : wtpRegistryList) {
                wtpRegistryService.removeRegistryById(wtpRegistry.getWtpRegistryId());
                log.error("wtp ------> removeRegistry WtpRegistry = [{}]. ", wtpRegistry);
            }
        }
    }

    public static void destroy() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }
}
