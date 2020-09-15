package wang.yeting.wtp.admin.model.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author : weipeng
 * @date : 2020-08-21 15:03
 */
@Getter
public enum PermissionEnum {

    ADMIN("ADMIN"), SELECT("SELECT"), INSERT("INSERT"), UPDATE("UPDATE"), DELETE("DELETE");

    private String permission;

    PermissionEnum(String permission) {
        this.permission = permission;
    }

    private static final Map<String, PermissionEnum> ENUM_MAP = new LinkedHashMap<>(PermissionEnum.values().length);

    static {
        for (PermissionEnum e : PermissionEnum.values()) {
            ENUM_MAP.put(e.getPermission(), e);
        }
    }

    public static PermissionEnum getByPermissionEnum(Integer status) {
        return ENUM_MAP.get(status);
    }

    public static Map<String, PermissionEnum> getEnumMap() {
        return ENUM_MAP;
    }

}
