package wang.yeting.wtp.admin.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-08-19 4:29 下午
 */
@Data
@Accessors(chain = true)
public class LoginVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

}
