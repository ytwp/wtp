package wang.yeting.wtp.admin.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-08-22 13:30
 */
@Data
@Accessors(chain = true)
public class UserAppPermissionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userAppPermissionId;

    private Integer userId;

    private String appId;
    private String permission;

}
