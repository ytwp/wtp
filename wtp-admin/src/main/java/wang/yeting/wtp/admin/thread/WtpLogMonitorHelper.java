package wang.yeting.wtp.admin.thread;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.factory.WtpFactory;
import wang.yeting.wtp.admin.service.EmailService;
import wang.yeting.wtp.core.biz.model.WtpLogBo;

/**
 * @author : weipeng
 * @date : 2020-07-30 19:56
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WtpLogMonitorHelper {

    private final EmailService emailService;

    private void doWtpLogMonitor(WtpLogBo wtpLogBo) {
        WtpFactory wtpFactory = WtpFactory.getInstance();
        Wtp wtp = wtpFactory.getWtp(wtpLogBo.getAppId(), wtpLogBo.getClusterId(), wtpLogBo.getName());
        if (!wtp.getOpenAlarm()) {
            return;
        }
        int poolActive = divide(wtpLogBo.getActiveCount(), wtpLogBo.getMaximumPoolSize());
        int queueActive = divide(wtpLogBo.getQueueSize(), wtpLogBo.getQueueSize() + wtpLogBo.getQueueRemainingCapacity());
        if (wtp.getPoolAlarmThreshold() > 0 && poolActive >= wtp.getPoolAlarmThreshold()) {
            String reason = "当前线程池活跃数：值为(" + poolActive + "%), 阈值为(" + wtp.getPoolAlarmThreshold() + "%)";
            emailService.sendMail(wtp.getAlarmEmail(), getAlarmText(wtpLogBo, wtp, reason), "线程池告警小助手");
        }
        if (wtp.getQueueAlarmThreshold() > 0 && queueActive >= wtp.getQueueAlarmThreshold()) {
            String reason = "当前队列使用数：值为(" + queueActive + "%), 阈值为(" + wtp.getQueueAlarmThreshold() + "%)";
            emailService.sendMail(wtp.getAlarmEmail(), getAlarmText(wtpLogBo, wtp, reason), "线程池告警小助手");
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

    private String getAlarmText(WtpLogBo wtpLogBo, Wtp wtp, String reason) {
        return " <br>应用：" + wtp.getAppId()
                + " <br>集群：" + wtp.getClusterId()
                + " <br>负责人：" + wtp.getOwnerName()
                + " <br>线程池名称：" + wtp.getName()
                + " <br>[告警原因]"
                + " <br>" + reason
                + " <br>[线程池参数]"
                + " <br>核心线程数：" + wtp.getCorePoolSize()
                + " <br>最大线程数：" + wtp.getMaximumPoolSize()
                + " <br>空闲线程销毁时间（秒）：" + wtp.getKeepAliveSeconds()
                + " <br>队列类型：" + wtp.getQueueName()
                + " <br>队列大小：" + wtp.getQueueCapacity()
                + " <br>[线程池当前信息]"
                + " <br>核心线程数：" + wtpLogBo.getCorePoolSize()
                + " <br>活动线程数：" + wtpLogBo.getActiveCount()
                + " <br>最大线程数：" + wtpLogBo.getMaximumPoolSize()
                + " <br>任务完成数：" + wtpLogBo.getCompletedTaskCount()
                + " <br>空闲线程销毁时间（秒）：" + wtpLogBo.getKeepAliveSeconds()
                + " <br>线程池活跃度：" + divide(wtpLogBo.getActiveCount(), wtpLogBo.getMaximumPoolSize())
                + " <br>队列大小：" + (wtpLogBo.getQueueSize() + wtpLogBo.getQueueRemainingCapacity())
                + " <br>当前排队线程数：" + wtpLogBo.getQueueSize()
                + " <br>队列剩余：" + wtpLogBo.getQueueRemainingCapacity()
                + " <br>队列使用度：" + divide(wtpLogBo.getQueueSize(), wtpLogBo.getQueueSize() + wtpLogBo.getQueueRemainingCapacity());
    }
}
