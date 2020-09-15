package wang.yeting.wtp.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.yeting.wtp.admin.bean.User;
import wang.yeting.wtp.admin.bean.UserAppPermission;
import wang.yeting.wtp.admin.mapper.UserMapper;
import wang.yeting.wtp.admin.model.Result;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.enums.UserEnum;
import wang.yeting.wtp.admin.model.vo.LoginVo;
import wang.yeting.wtp.admin.service.LoginService;
import wang.yeting.wtp.admin.service.UserAppPermissionService;
import wang.yeting.wtp.admin.util.RedisUtils;
import wang.yeting.wtp.admin.util.TokenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : weipeng
 * @date : 2020-08-19 3:34 下午
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    private final UserAppPermissionService userAppPermissionService;
    private final RedisUtils redisUtils;
    private final TokenUtils tokenUtils;

    @Override
    public Result login(LoginVo loginVo) {
        String password = tokenUtils.encryptedPassword(loginVo.getPassword());
        User user = getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, loginVo.getUsername()).eq(User::getPassword, password));
        if (user == null) {
            //The account does not exist
            return Result.exceptionError("The account does not exist");
        }
        if (!Objects.equals(user.getStatus(), UserEnum.normal.getStatus())) {
            //The account is not normal
            return Result.exceptionError("The account is not normal");
        }
        String token = tokenUtils.generateToken();
        //permission  and  userinfo
        permissionAndUser(user, token);
        return Result.success(MapUtil.of("token", token));
    }

    @Override
    public Result info(String token) {
        UserBo userBo = redisUtils.get(TokenUtils.TOKEN_PREFIX + token, UserBo.class);
        return Result.success(userBo);
    }

    private void permissionAndUser(User user, String token) {
        UserBo userBo = new UserBo();
        BeanUtil.copyProperties(user, userBo);
        List<String> roles = new ArrayList<>();
        roles.add(user.getRole());
        userBo.setRoles(roles);
        userBo.setToken(token);
        List<String> permissions = new ArrayList<>();
        Integer userId = user.getUserId();
        List<UserAppPermission> userAppPermissionList = userAppPermissionService.getByUserId(userId);
        userBo.setUserAppPermissionList(userAppPermissionList);
        for (UserAppPermission userAppPermission : userAppPermissionList) {
            permissions.add(userAppPermission.getAppId() + TokenUtils.PERMISSION_SPACE_MARK + userAppPermission.getPermission());
        }
        userBo.setPermissions(permissions);
        log.error("userBo_{}", userBo);
        redisUtils.set(TokenUtils.TOKEN_PREFIX + token, userBo, 7200);
    }

}
