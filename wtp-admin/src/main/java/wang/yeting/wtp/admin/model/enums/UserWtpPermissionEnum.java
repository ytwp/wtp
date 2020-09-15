package wang.yeting.wtp.admin.model.enums;

import lombok.Getter;

/**
 * @author : weipeng
 * @date : 2020-08-19 22:25
 */
@Getter
public enum UserWtpPermissionEnum {

    normal(10, "Account is normal"), abnormal(-10, "Account is abnormal");

    private Integer status;
    private String message;

    UserWtpPermissionEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
