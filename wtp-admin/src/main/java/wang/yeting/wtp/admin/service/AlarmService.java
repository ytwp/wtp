package wang.yeting.wtp.admin.service;

import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.core.biz.model.WtpLogBo;

/**
 * @author : weipeng
 * @date : 2020-10-28 5:29 下午
 */

public interface AlarmService {

    Boolean poolAlarm(Wtp wtp, WtpLogBo wtpLogBo);

    Boolean queueAlarm(Wtp wtp, WtpLogBo wtpLogBo);

}
