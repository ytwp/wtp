package wang.yeting.wtp.admin.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-08-19 3:59 下午
 */
@Data
@Accessors(chain = true)
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;

    private String nickname;
    private String username;
    private String phone;
    private String email;

    private Integer page = 1;
    private Integer size = 10;
}
