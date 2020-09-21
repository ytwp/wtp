package wang.yeting.wtp.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.yeting.wtp.admin.bean.User;
import wang.yeting.wtp.admin.exceptions.PermissionException;
import wang.yeting.wtp.admin.mapper.UserMapper;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.ResultCode;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.dto.UserDto;
import wang.yeting.wtp.admin.model.enums.RoleEnum;
import wang.yeting.wtp.admin.model.enums.UserEnum;
import wang.yeting.wtp.admin.model.vo.UserVo;
import wang.yeting.wtp.admin.service.UserService;
import wang.yeting.wtp.admin.util.TokenUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : weipeng
 * @date : 2020-08-19 3:34 下午
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final TokenUtils tokenUtils;

    @SneakyThrows
    @Override
    public PageResponse<UserDto> page(UserVo userVo, UserBo userBo) {
        if (!tokenUtils.checkAdmin(userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>()
                .eq(userVo.getUserId() != null, User::getUserId, userVo.getUserId())
                .eq(StringUtils.isNotBlank(userVo.getEmail()), User::getEmail, userVo.getEmail())
                .eq(StringUtils.isNotBlank(userVo.getPhone()), User::getPhone, userVo.getPhone())
                .like(StringUtils.isNotBlank(userVo.getNickname()), User::getNickname, userVo.getNickname())
                .like(StringUtils.isNotBlank(userVo.getUsername()), User::getUsername, userVo.getUsername());
        if (!tokenUtils.checkSuperAdmin(userBo)) {
            lambdaQueryWrapper.in(User::getRole, RoleEnum.ADMIN.getRole(), RoleEnum.USER.getRole());
        }
        IPage<User> page = page(new Page<>(userVo.getPage(), userVo.getSize()), lambdaQueryWrapper);
        List<UserDto> userDtoList = page.getRecords().stream().map(user -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            userDto.setStatus(UserEnum.getByUserEnum(user.getStatus()).getMessage());
            return userDto;
        }).collect(Collectors.toList());
        return new PageResponse<UserDto>().setList(userDtoList).setPage(page.getPages()).setTotal(page.getTotal());
    }

    @SneakyThrows
    @Override
    public Boolean update(User user, UserBo userBo) {
        if (!tokenUtils.checkAdmin(userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        User userDb = getById(user.getUserId());
        if (!tokenUtils.checkSuperAdmin(userBo) && tokenUtils.checkAdmin(userDb)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(tokenUtils.encryptedPassword(user.getPassword()));
        }
        return updateById(user);
    }

    @SneakyThrows
    @Override
    public Boolean create(User user, UserBo userBo) {
        if (!tokenUtils.checkAdmin(userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        if (!tokenUtils.checkSuperAdmin(userBo) && tokenUtils.checkAdmin(user)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        user.setPassword(tokenUtils.encryptedPassword(user.getPassword()));
        return save(user);
    }

}
