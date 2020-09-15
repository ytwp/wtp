package wang.yeting.wtp.admin.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : weipeng
 * @date : 2020-08-19 5:07 下午
 */
@Getter
public enum UserEnum {

    normal(10, "normal"), abnormal(-10, "abnormal");

    private Integer status;
    private String message;

    UserEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    private static final Map<Integer, UserEnum> ENUM_MAP = new HashMap<>(UserEnum.values().length);

    static {
        for (UserEnum e : UserEnum.values()) {
            ENUM_MAP.put(e.getStatus(), e);
        }
    }

    public static UserEnum getByUserEnum(Integer status) {
        return ENUM_MAP.get(status);
    }

}
