package wang.yeting.wtp.admin.model.bo;

import lombok.Data;
import lombok.experimental.Accessors;
import wang.yeting.wtp.admin.bean.UserAppPermission;

import java.io.Serializable;
import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-08-19 8:04 下午
 */
@Data
@Accessors(chain = true)
public class UserBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String token;
    private String nickname;
    private String avatar;
    private String username;
    private String phone;
    private String email;
    private List<String> roles;
    private List<String> permissions;
    private List<UserAppPermission> userAppPermissionList;
}
