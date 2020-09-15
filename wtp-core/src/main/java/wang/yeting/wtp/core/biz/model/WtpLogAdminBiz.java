package wang.yeting.wtp.core.biz.model;

import lombok.Data;
import lombok.experimental.Accessors;
import wang.yeting.wtp.core.biz.client.AdminBiz;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-24 19:21
 */

@Data
@Accessors(chain = true)
public class WtpLogAdminBiz implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer waitCount;
    private AdminBiz adminBiz;

}
