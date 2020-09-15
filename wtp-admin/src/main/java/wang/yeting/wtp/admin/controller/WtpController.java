package wang.yeting.wtp.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.yeting.wtp.admin.annotation.CurrentUser;
import wang.yeting.wtp.admin.annotation.Permission;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.Result;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.vo.WtpVo;
import wang.yeting.wtp.admin.service.WtpService;
import wang.yeting.wtp.core.enums.QueueEnums;
import wang.yeting.wtp.core.enums.RejectedExecutionHandlerEnums;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:51
 */
@Permission
@CrossOrigin
@RestController
@RequestMapping("/wtp")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WtpController {

    private final WtpService wtpService;

    @GetMapping("/page")
    public Result<PageResponse> page(WtpVo WtpVo, @CurrentUser UserBo userBo) {
        PageResponse page = wtpService.page(WtpVo, userBo);
        return Result.success(page);
    }

    @PostMapping("/create")
    public Result<Boolean> create(Wtp wtp, @CurrentUser UserBo userBo) {
        Boolean create = wtpService.create(wtp, userBo);
        return Result.success(create);
    }

    @PostMapping("/update")
    public Result<Boolean> update(Wtp wtp, @CurrentUser UserBo userBo) {
        Boolean create = wtpService.update(wtp, userBo);
        return Result.success(create);
    }

    @PostMapping("/del")
    public Result<Boolean> del(Wtp wtp, @CurrentUser UserBo userBo) {
        Boolean del = wtpService.del(wtp, userBo);
        return Result.success(del);
    }

    @GetMapping("/get")
    public Result<Wtp> get(WtpVo WtpVo, @CurrentUser UserBo userBo) {
        Wtp wtp = wtpService.get(WtpVo, userBo);
        return Result.success(wtp);
    }

    @GetMapping("/queueOptions")
    public Result<PageResponse> queueOptions() {
        return Result.success(new PageResponse().setList(QueueEnums.getAllQueueName()));
    }

    @GetMapping("/rejectedOptions")
    public Result<PageResponse> rejectedOptions() {
        return Result.success(new PageResponse().setList(RejectedExecutionHandlerEnums.getAllRejectedExecutionHandlerName()));
    }
}
