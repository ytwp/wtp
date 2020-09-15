package wang.yeting.wtp.admin.thread;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.admin.bean.WtpRegistry;
import wang.yeting.wtp.admin.service.WtpRegistryService;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-30 19:56
 */

@Slf4j
public class WtpRegistryMonitorHelper {

    private WtpRegistryService wtpRegistryService;

    public WtpRegistryMonitorHelper(WtpRegistryService wtpRegistryService) {
        this.wtpRegistryService = wtpRegistryService;
    }

    public void registryMonitor() {
        while (true) {
            try {
                doRegistryMonitor();
                Thread.sleep(300000L);
            } catch (Exception e) {
                log.error("wtp ------> doPushLog Exception = [{}]. ", e);
            }
        }
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

}
