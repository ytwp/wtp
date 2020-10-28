package wang.yeting.wtp.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.service.AlarmService;
import wang.yeting.wtp.admin.service.alarm.WtpAlarm;
import wang.yeting.wtp.core.biz.model.WtpLogBo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : weipeng
 * @date : 2020-10-28 5:35 下午
 */
@Slf4j
@Service
public class AlarmServiceImpl implements AlarmService, ApplicationContextAware {

    private List<WtpAlarm> wtpAlarmList;

    /**
     * 获取所以告警实现
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, WtpAlarm> wtpAlarmMap = applicationContext.getBeansOfType(WtpAlarm.class);
        wtpAlarmList = new ArrayList<>(wtpAlarmMap.values());
    }

    /**
     * 有一个发送失败，即为失败
     *
     * @param wtp
     * @param wtpLogBo
     * @return
     */
    @Override
    public Boolean poolAlarm(Wtp wtp, WtpLogBo wtpLogBo) {
        Boolean result = true;
        for (WtpAlarm wtpAlarm : wtpAlarmList) {
            try {
                Boolean poolAlarm = wtpAlarm.poolAlarm(wtp, wtpLogBo);
                if (!poolAlarm) {
                    result = false;
                }
            } catch (Exception e) {
                log.error("poolAlarm_", e);
                result = false;
            }
        }
        return result;
    }

    @Override
    public Boolean queueAlarm(Wtp wtp, WtpLogBo wtpLogBo) {
        Boolean result = true;
        for (WtpAlarm wtpAlarm : wtpAlarmList) {
            try {
                Boolean poolAlarm = wtpAlarm.queueAlarm(wtp, wtpLogBo);
                if (!poolAlarm) {
                    result = false;
                }
            } catch (Exception e) {
                log.error("queueAlarm_", e);
                result = false;
            }
        }
        return result;
    }
}
