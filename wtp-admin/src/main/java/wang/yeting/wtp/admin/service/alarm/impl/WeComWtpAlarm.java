package wang.yeting.wtp.admin.service.alarm.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.model.alarm.dingtalk.DingTalkMsg;
import wang.yeting.wtp.admin.model.alarm.wecom.WeComMsg;
import wang.yeting.wtp.admin.service.alarm.WtpAlarm;
import wang.yeting.wtp.core.biz.model.WtpLogBo;

/**
 * @author : weipeng
 * @date : 2020-10-29 3:58 下午
 * 企业微信机器人告警
 */
@Component
@ConditionalOnProperty(name = "weCom.alarm.enabled", havingValue = "true")
public class WeComWtpAlarm implements WtpAlarm {

    @Value("${weCom.webhook}")
    private String webhook;

    @Override
    public Boolean poolAlarm(Wtp wtp, WtpLogBo wtpLogBo) {
        int poolActive = divide(wtpLogBo.getActiveCount(), wtpLogBo.getMaximumPoolSize());
        String reason = "当前线程池活跃率为：(" + poolActive + "%), 阈值为：(" + wtp.getPoolAlarmThreshold() + "%)";
        return sendMsg(getAlarmText(wtpLogBo, wtp, reason));
    }

    @Override
    public Boolean queueAlarm(Wtp wtp, WtpLogBo wtpLogBo) {
        int queueActive = divide(wtpLogBo.getQueueSize(), wtpLogBo.getQueueSize() + wtpLogBo.getQueueRemainingCapacity());
        String reason = "当前队列使用率为：(" + queueActive + "%), 阈值为：(" + wtp.getQueueAlarmThreshold() + "%)";
        return sendMsg(getAlarmText(wtpLogBo, wtp, reason));
    }

    private Boolean sendMsg(String text) {
        try {
            WeComMsg weComMsg = new WeComMsg();
            WeComMsg.Msg msg = weComMsg.new Msg();
            msg.setContent(text);
            // @某人  手机号
            //msg.ofMentionedMobileList("xxxx","xxx");
            // @全部
            msg.ofMentionedMobileList("@all");
            weComMsg.setText(msg);
            //发送
            String post = HttpUtil.post(webhook, JSONUtil.toJsonStr(weComMsg));
            JSONObject jsonObject = JSONUtil.parseObj(post);
            Integer errcode = jsonObject.getInt("errcode");
            return errcode == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int divide(int num1, long num2) {
        return (int) (Double.parseDouble(num1 + "") / Double.parseDouble(num2 + "") * 100);
    }

    private String getAlarmText(WtpLogBo wtpLogBo, Wtp wtp, String reason) {
        return "线程池告警小助手"
                + "\n应用：" + wtp.getAppId()
                + "\n集群：" + wtp.getClusterId()
                + "\n负责人：" + wtp.getOwnerName()
                + "\n线程池名称：" + wtp.getName()
                + "\n[告警原因]"
                + "\n" + reason
                + "\n[线程池参数]"
                + "\n核心线程数：" + wtp.getCorePoolSize()
                + "\n最大线程数：" + wtp.getMaximumPoolSize()
                + "\n空闲线程销毁时间（秒）：" + wtp.getKeepAliveSeconds()
                + "\n队列类型：" + wtp.getQueueName()
                + "\n队列大小：" + wtp.getQueueCapacity()
                + "\n[线程池当前信息]"
                + "\n核心线程数：" + wtpLogBo.getCorePoolSize()
                + "\n活动线程数：" + wtpLogBo.getActiveCount()
                + "\n最大线程数：" + wtpLogBo.getMaximumPoolSize()
                + "\n任务完成数：" + wtpLogBo.getCompletedTaskCount()
                + "\n空闲线程销毁时间（秒）：" + wtpLogBo.getKeepAliveSeconds()
                + "\n线程池活跃度：" + divide(wtpLogBo.getActiveCount(), wtpLogBo.getMaximumPoolSize())
                + "\n队列大小：" + (wtpLogBo.getQueueSize() + wtpLogBo.getQueueRemainingCapacity())
                + "\n当前排队线程数：" + wtpLogBo.getQueueSize()
                + "\n队列剩余：" + wtpLogBo.getQueueRemainingCapacity()
                + "\n队列使用度：" + divide(wtpLogBo.getQueueSize(), wtpLogBo.getQueueSize() + wtpLogBo.getQueueRemainingCapacity());
    }
}
