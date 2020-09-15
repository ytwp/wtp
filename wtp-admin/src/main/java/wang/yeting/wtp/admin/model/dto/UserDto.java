package wang.yeting.wtp.admin.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-27 11:42
 */

@Data
@Accessors(chain = true)
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String nickname;
    private String avatar;
    private String username;
    private String phone;
    private String email;
    private String role;

    private String status;
    private Long created;
}
