package wang.yeting.wtp.admin.thread;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.factory.WtpFactory;
import wang.yeting.wtp.admin.service.AlarmService;
import wang.yeting.wtp.core.biz.model.WtpLogBo;

/**
 * @author : weipeng
 * @date : 2020-07-30 19:56
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WtpLogMonitorHandler {

    private final AlarmService alarmService;

    private void doWtpLogMonitor(WtpLogBo wtpLogBo) {
        WtpFactory wtpFactory = WtpFactory.getInstance();
        Wtp wtp = wtpFactory.getWtp(wtpLogBo.getAppId(), wtpLogBo.getClusterId(), wtpLogBo.getName());
        if (!wtp.getOpenAlarm()) {
            return;
        }
        int poolActive = divide(wtpLogBo.getActiveCount(), wtpLogBo.getMaximumPoolSize());
        int queueActive = divide(wtpLogBo.getQueueSize(), wtpLogBo.getQueueSize() + wtpLogBo.getQueueRemainingCapacity());
        if (wtp.getPoolAlarmThreshold() > 0 && poolActive >= wtp.getPoolAlarmThreshold()) {
            Boolean poolAlarm = alarmService.poolAlarm(wtp, wtpLogBo);
            log.warn("poolAlarm " + (poolAlarm ? "告警成功" : "告警失败"));
        }
        if (wtp.getQueueAlarmThreshold() > 0 && queueActive >= wtp.getQueueAlarmThreshold()) {
            Boolean queueAlarm = alarmService.queueAlarm(wtp, wtpLogBo);
            log.warn("queueAlarm " + (queueAlarm ? "告警成功" : "告警失败"));
        }
    }

    public void wtpLogMonitor(WtpLogBo wtpLogBo) {
        try {
            MainThreadPool.execute(() -> {
                        doWtpLogMonitor(wtpLogBo);
                    }
            );
        } catch (Exception e) {
            log.error("wtp ------> wtpLogMonitor Exception = [{}]. ", e);
        }
    }

    private int divide(int num1, long num2) {
        return (int) (Double.parseDouble(num1 + "") / Double.parseDouble(num2 + "") * 100);
    }
}
