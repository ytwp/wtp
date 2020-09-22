package wang.yeting.wtp.admin.util;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wang.yeting.wtp.admin.bean.User;
import wang.yeting.wtp.admin.bean.UserAppPermission;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.enums.PermissionEnum;
import wang.yeting.wtp.admin.model.enums.RoleEnum;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author : weipeng
 * @date : 2020-08-20 19:05
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenUtils {

    private final RedisUtils redisUtils;

    private static final String PASSWORD_SUFFIX = "wtp";

    public static final String TOKEN_PREFIX = "login:";

    public static final String PERMISSION_SPACE_MARK = "+";

    public UserBo getUserBoByToken(String token) {
        return redisUtils.get(TOKEN_PREFIX + token, UserBo.class);
    }

    public boolean checkSuperAdmin(UserBo userBo) {
        return userBo.getRoles().contains(RoleEnum.SUPER_ADMIN.getRole());
    }

    public boolean checkAdmin(UserBo userBo) {
        boolean checkSuperAdmin = checkSuperAdmin(userBo);
        if (checkSuperAdmin) {
            return true;
        }
        return userBo.getRoles().contains(RoleEnum.ADMIN.getRole());
    }

    public boolean checkAppAdmin(String appId, UserBo userBo) {
        boolean checkAdmin = checkAdmin(userBo);
        if (checkAdmin) {
            return true;
        }
        return userBo.getPermissions().contains(appId + PERMISSION_SPACE_MARK + PermissionEnum.ADMIN.getPermission());
    }

    public boolean checkAppPermission(String appId, String Permission, UserBo userBo) {
        boolean checkAppAdmin = checkAppAdmin(appId, userBo);
        if (checkAppAdmin) {
            return true;
        }
        return userBo.getPermissions().contains(appId + PERMISSION_SPACE_MARK + Permission);
    }

    public boolean checkRolePermission(String[] permissions, List<String> permissionList) {
        for (String permission : permissions) {
            boolean contains = permissionList.contains(permission);
            if (contains) {
                return true;
            }
        }
        return false;
    }

    public boolean checkRole(RoleEnum[] roles, List<String> roleList) {
        for (RoleEnum roleEnum : roles) {
            boolean contains = roleList.contains(roleEnum.getRole());
            if (contains) {
                return true;
            }
        }
        return false;
    }

    public boolean checkAdmin(User user) {
        boolean checkSuperAdmin = checkSuperAdmin(user);
        if (checkSuperAdmin) {
            return true;
        }
        return Objects.equals(user.getRole(), RoleEnum.ADMIN.getRole());
    }

    public boolean checkSuperAdmin(User user) {
        return Objects.equals(user.getRole(), RoleEnum.SUPER_ADMIN.getRole());
    }

    public String encryptedPassword(String password) {
        return DigestUtils.md5Hex(DigestUtils.md5Hex(password) + PASSWORD_SUFFIX);
    }

    public String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public void addPermission(UserAppPermission userAppPermission, UserBo userBo) {
        List<UserAppPermission> userAppPermissionList = userBo.getUserAppPermissionList();
        List<String> permissions = userBo.getPermissions();
        if (CollectionUtil.isNotEmpty(userAppPermissionList) && CollectionUtil.isNotEmpty(permissions)) {
            userAppPermissionList.add(userAppPermission);
            permissions.add(userAppPermission.getAppId() + PERMISSION_SPACE_MARK + userAppPermission.getPermission());
            redisUtils.set(TOKEN_PREFIX + userBo.getToken(), userBo, 7200);
        }
    }

    public boolean logout(String token) {
        return redisUtils.delete(TOKEN_PREFIX + token);
    }
}
