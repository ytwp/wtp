package wang.yeting.wtp.admin.service.alarm.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.service.alarm.WtpAlarm;
import wang.yeting.wtp.core.biz.model.WtpLogBo;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

/**
 * @author : weipeng
 * @date : 2020-10-28 5:15 下午
 */
@Component
public class EmailWtpAlarm implements WtpAlarm {

    @Value("${email.user}")
    private String user;
    @Value("${email.password}")
    private String password;
    @Value("${email.mailHost:smtp.163.com}")
    private String mailHost;

    @Override
    public Boolean poolAlarm(Wtp wtp, WtpLogBo wtpLogBo) {
        int poolActive = divide(wtpLogBo.getActiveCount(), wtpLogBo.getMaximumPoolSize());
        String reason = "当前线程池活跃率为：(" + poolActive + "%), 阈值为：(" + wtp.getPoolAlarmThreshold() + "%)";
        return sendMail(wtp.getAlarmEmail(), getAlarmText(wtpLogBo, wtp, reason), "线程池告警小助手");
    }

    @Override
    public Boolean queueAlarm(Wtp wtp, WtpLogBo wtpLogBo) {
        int queueActive = divide(wtpLogBo.getQueueSize(), wtpLogBo.getQueueSize() + wtpLogBo.getQueueRemainingCapacity());
        String reason = "当前队列使用率为：(" + queueActive + "%), 阈值为：(" + wtp.getQueueAlarmThreshold() + "%)";
        return sendMail(wtp.getAlarmEmail(), getAlarmText(wtpLogBo, wtp, reason), "线程池告警小助手");
    }

    private Boolean sendMail(String to, String text, String title) {
        try {
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "SMTP");
            props.setProperty("mail.host", mailHost);
            props.setProperty("mail.smtp.auth", "true");
            Authenticator auth = new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
                }
            };
            Session session = Session.getInstance(props, auth);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            message.setSubject(title);
            Multipart multipart = new MimeMultipart();
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(text, "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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
