package wang.yeting.wtp.admin.model.enums;

import lombok.Getter;

/**
 * @author : weipeng
 * @date : 2020-08-19 5:07 下午
 */
@Getter
public enum RoleEnum {

    SUPER_ADMIN("SUPPER-ADMIN"), ADMIN("ADMIN"), USER("USER");

    private String role;

    RoleEnum(String role) {
        this.role = role;
    }
}
