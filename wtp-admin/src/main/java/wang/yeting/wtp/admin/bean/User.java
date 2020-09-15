package wang.yeting.wtp.admin.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-27 11:42
 */

@Data
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer userId;

    private String nickname;
    private String avatar;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String role;

    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private Long created;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long modified;
}
