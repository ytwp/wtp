package wang.yeting.wtp.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.yeting.wtp.admin.annotation.CurrentUser;
import wang.yeting.wtp.admin.annotation.Permission;
import wang.yeting.wtp.admin.bean.App;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.Result;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.enums.RoleEnum;
import wang.yeting.wtp.admin.model.vo.AppVo;
import wang.yeting.wtp.admin.service.AppService;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:50
 */
@Permission
@CrossOrigin
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppController {

    private final AppService appService;

    @GetMapping("/page")
    public Result<PageResponse<App>> page(AppVo appVo, @CurrentUser UserBo userBo) {
        PageResponse<App> page = appService.page(appVo, userBo);
        return Result.success(page);
    }

    @PostMapping("/create")
    public Result<Boolean> create(App app, @CurrentUser UserBo userBo) {
        Boolean create = appService.create(app, userBo);
        return Result.success(create);
    }

    @PostMapping("/update")
    public Result<Boolean> update(App app, @CurrentUser UserBo userBo) {
        Boolean create = appService.update(app, userBo);
        return Result.success(create);
    }

    @GetMapping("/options")
    public Result<PageResponse<App>> options(@CurrentUser UserBo userBo) {
        PageResponse<App> page = appService.appOptions(userBo);
        return Result.success(page);
    }

    @GetMapping("/get")
    public Result<App> get(AppVo appVo, @CurrentUser UserBo userBo) {
        App app = appService.get(appVo, userBo);
        return Result.success(app);
    }

    @Permission(roles = RoleEnum.SUPER_ADMIN)
    @GetMapping("/del")
    public Result<Boolean> del(AppVo appVo, @CurrentUser UserBo userBo) {
        Boolean del = appService.del(appVo, userBo);
        return Result.success(del);
    }
}
