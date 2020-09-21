package wang.yeting.wtp.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.yeting.wtp.admin.bean.App;
import wang.yeting.wtp.admin.bean.UserAppPermission;
import wang.yeting.wtp.admin.exceptions.PermissionException;
import wang.yeting.wtp.admin.mapper.AppMapper;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.ResultCode;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.enums.PermissionEnum;
import wang.yeting.wtp.admin.model.vo.AppVo;
import wang.yeting.wtp.admin.service.AppService;
import wang.yeting.wtp.admin.service.UserAppPermissionService;
import wang.yeting.wtp.admin.util.TokenUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:48
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    private final TokenUtils tokenUtils;
    private final UserAppPermissionService userAppPermissionService;

    @SneakyThrows
    @Override
    public PageResponse<App> page(AppVo appVo, UserBo userBo) {
        if (!tokenUtils.checkAdmin(userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        LambdaQueryWrapper<App> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!tokenUtils.checkSuperAdmin(userBo)) {
            List<UserAppPermission> userAppPermissionList = userBo.getUserAppPermissionList();
            Set<String> permissionSet = userAppPermissionList.stream().map(UserAppPermission::getAppId).collect(Collectors.toSet());
            if (CollectionUtil.isEmpty(permissionSet)) {
                return new PageResponse<App>().setList(null);
            }
            lambdaQueryWrapper.in(App::getAppId, permissionSet);
        }
        IPage<App> iPage = page(new Page<>(appVo.getPage(), appVo.getSize()), lambdaQueryWrapper);
        return new PageResponse<App>().setList(iPage.getRecords()).setPage(iPage.getPages()).setTotal(iPage.getTotal());
    }

    @SneakyThrows
    @Override
    public Boolean create(App app, UserBo userBo) {
        if (!tokenUtils.checkAdmin(userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        int count = count(new LambdaQueryWrapper<App>().eq(App::getAppId, app.getAppId()));
        if (count > 0) {
            throw new PermissionException("App 已经存在");
        }
        //不是 SuperAdmin 授权
        if (!tokenUtils.checkSuperAdmin(userBo)) {
            UserAppPermission userAppPermission = new UserAppPermission().setAppId(app.getAppId()).setUserId(userBo.getUserId()).setPermission(PermissionEnum.ADMIN.getPermission());
            userAppPermissionService.add(userAppPermission);
            tokenUtils.addPermission(userAppPermission, userBo);
        }
        return save(app);
    }

    @Override
    public PageResponse<App> appOptions(UserBo userBo) {
        LambdaQueryWrapper<App> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(App::getAppId, App::getAppName);
        if (!tokenUtils.checkSuperAdmin(userBo)) {
            List<UserAppPermission> userAppPermissionList = userBo.getUserAppPermissionList();
            Set<String> permissionSet = userAppPermissionList.stream().map(UserAppPermission::getAppId).collect(Collectors.toSet());
            if (CollectionUtil.isEmpty(permissionSet)) {
                return new PageResponse<App>().setList(null);
            }
            lambdaQueryWrapper.in(App::getAppId, permissionSet);
        }
        List<App> list = list(lambdaQueryWrapper);
        return new PageResponse<App>().setList(list);
    }

    @SneakyThrows
    @Override
    public Boolean update(App app, UserBo userBo) {
        if (!tokenUtils.checkSuperAdmin(userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        return updateById(app);
    }

    @SneakyThrows
    @Override
    public App get(AppVo appVo, UserBo userBo) {
        if (!tokenUtils.checkAdmin(userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        return getById(appVo.getId());
    }

    @Override
    public Boolean del(AppVo appVo, UserBo userBo) {
        return removeById(appVo.getId());
    }
}
