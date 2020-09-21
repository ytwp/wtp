package wang.yeting.wtp.admin.service;

import wang.yeting.wtp.admin.bean.WtpLog;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.vo.WtpLogVo;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:44
 */

public interface WtpLogService {

    /**
     * last WtpLog
     *
     * @param wtpLogVo
     * @return
     */
    WtpLog realTime(WtpLogVo wtpLogVo);

    /**
     * create a WtpLog
     *
     * @param wtpLog
     * @return
     */
    Boolean create(WtpLog wtpLog);

    /**
     * Chart of last 30 minutes
     *
     * @param wtpLogVo
     * @return
     */
    List<WtpLog> chart(WtpLogVo wtpLogVo);

    /**
     * list
     *
     * @param wtpLogVo
     * @return
     */
    PageResponse<WtpLog> page(WtpLogVo wtpLogVo);
}
