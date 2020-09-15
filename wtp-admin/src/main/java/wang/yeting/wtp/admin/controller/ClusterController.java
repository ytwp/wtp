package wang.yeting.wtp.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import wang.yeting.wtp.admin.annotation.CurrentUser;
import wang.yeting.wtp.admin.annotation.Permission;
import wang.yeting.wtp.admin.bean.Cluster;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.Result;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.vo.ClusterVo;
import wang.yeting.wtp.admin.service.ClusterService;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:51
 */
@Permission
@CrossOrigin
@RestController
@RequestMapping("/cluster")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClusterController {

    private final ClusterService clusterService;

    @GetMapping("/page")
    public Result<PageResponse> page(ClusterVo ClusterVo, @CurrentUser UserBo userBo) {
        PageResponse page = clusterService.page(ClusterVo, userBo);
        return Result.success(page);
    }

    @PostMapping("/create")
    public Result<?> create(Cluster cluster, @CurrentUser UserBo userBo) {
        Boolean create = clusterService.create(cluster, userBo);
        return Result.success(create);
    }

    @PostMapping("/update")
    public Result<?> update(Cluster cluster, @CurrentUser UserBo userBo) {
        Boolean update = clusterService.update(cluster, userBo);
        return Result.success(update);
    }

    @GetMapping("/options")
    public Result<PageResponse> options(ClusterVo ClusterVo, @CurrentUser UserBo userBo) {
        PageResponse page = clusterService.options(ClusterVo, userBo);
        return Result.success(page);
    }

    @GetMapping("/get")
    public Result<Cluster> get(ClusterVo ClusterVo, @CurrentUser UserBo userBo) {
        Cluster cluster = clusterService.get(ClusterVo, userBo);
        return Result.success(cluster);
    }

}
