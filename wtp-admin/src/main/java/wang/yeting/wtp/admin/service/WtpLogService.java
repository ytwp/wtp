package wang.yeting.wtp.admin.service;

import wang.yeting.wtp.admin.bean.WtpLog;
import wang.yeting.wtp.admin.model.vo.WtpLogVo;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:44
 */

public interface WtpLogService {
    WtpLog realTime(WtpLogVo WtpLogVo);

    Boolean create(WtpLog wtpLog);

    List<WtpLog> chart(WtpLogVo WtpLogVo);
}
