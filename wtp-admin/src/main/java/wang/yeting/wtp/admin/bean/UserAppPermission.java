package wang.yeting.wtp.admin.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-08-19 21:17
 */
@Data
@Accessors(chain = true)
public class UserAppPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String PERMISSION_ADMIN = "admin";
    private static final String PERMISSION_USER = "user";

    @TableId(type = IdType.AUTO)
    private Integer userAppPermissionId;

    private Integer userId;

    private String appId;
    private String permission;

    @TableField(fill = FieldFill.INSERT)
    private Long created;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long modified;
}
