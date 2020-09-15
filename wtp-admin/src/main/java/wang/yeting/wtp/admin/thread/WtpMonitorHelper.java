package wang.yeting.wtp.admin.thread;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.factory.WtpConfigFactory;
import wang.yeting.wtp.admin.factory.WtpFactory;
import wang.yeting.wtp.admin.service.WtpService;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-30 19:56
 */

@Slf4j
public class WtpMonitorHelper {

    private WtpService wtpService;

    public WtpMonitorHelper(WtpService wtpService) {
        this.wtpService = wtpService;
    }

    public void wtpMonitor() {
        while (true) {
            try {
                doWtpMonitor();
                Thread.sleep(300000L);
            } catch (Exception e) {
                log.error("wtp ------> doPushLog Exception = [{}]. ", e);
            }
        }
    }

    private void doWtpMonitor() {
        log.info("wtp ------> doRegistryMonitor . ");
        List<Wtp> wtpList = wtpService.initConfigFactory();
        WtpFactory wtpFactory = WtpFactory.getInstance();
        wtpFactory.loadWtp(wtpList);
        WtpConfigFactory wtpConfigFactory = WtpConfigFactory.getInstance();
        wtpConfigFactory.loadConfig(wtpList);
    }

}
