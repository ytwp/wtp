package wang.yeting.wtp.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.yeting.wtp.admin.annotation.CurrentUser;
import wang.yeting.wtp.admin.annotation.Permission;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.Result;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.enums.RoleEnum;
import wang.yeting.wtp.admin.model.vo.UserAppPermissionVo;
import wang.yeting.wtp.admin.service.UserAppPermissionService;

/**
 * @author : weipeng
 * @date : 2020-08-22 13:25
 */
@Permission(roles = {RoleEnum.SUPER_ADMIN, RoleEnum.ADMIN})
@CrossOrigin
@RestController
@RequestMapping("/userAppPermission")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAppPermissionController {

    private final UserAppPermissionService userAppPermissionService;

    @GetMapping("/find")
    public Result<PageResponse> find(UserAppPermissionVo userAppPermissionVo, @CurrentUser UserBo userBo) {
        PageResponse pageResponse = userAppPermissionService.find(userAppPermissionVo, userBo);
        return Result.success(pageResponse);
    }

    @PostMapping("/create")
    public Result<Boolean> create(UserAppPermissionVo userAppPermissionVo, @CurrentUser UserBo userBo) {
        Boolean create = userAppPermissionService.create(userAppPermissionVo, userBo);
        return Result.success(create);
    }

    @PostMapping("/del")
    public Result<Boolean> update(UserAppPermissionVo userAppPermissionVo, @CurrentUser UserBo userBo) {
        Boolean create = userAppPermissionService.del(userAppPermissionVo, userBo);
        return Result.success(create);
    }
}
