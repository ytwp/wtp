package wang.yeting.wtp.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.yeting.wtp.admin.annotation.CurrentUser;
import wang.yeting.wtp.admin.annotation.Permission;
import wang.yeting.wtp.admin.bean.User;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.Result;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.dto.UserDto;
import wang.yeting.wtp.admin.model.vo.UserVo;
import wang.yeting.wtp.admin.service.UserService;

/**
 * @author : weipeng
 * @date : 2020-08-19 3:37 下午
 */
@Permission
@CrossOrigin
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;

    @GetMapping("/page")
    public Result<PageResponse<UserDto>> page(UserVo userVo, @CurrentUser UserBo userBo) {
        PageResponse<UserDto> page = userService.page(userVo, userBo);
        return Result.success(page);
    }

    @PostMapping("/create")
    public Result<Boolean> create(User user, @CurrentUser UserBo userBo) {
        Boolean create = userService.create(user, userBo);
        return Result.success(create);
    }

    @PostMapping("/update")
    public Result<Boolean> update(User user, @CurrentUser UserBo userBo) {
        Boolean create = userService.update(user, userBo);
        return Result.success(create);
    }
}
