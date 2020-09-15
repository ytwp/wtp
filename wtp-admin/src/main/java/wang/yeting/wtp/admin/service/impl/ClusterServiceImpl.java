package wang.yeting.wtp.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.yeting.wtp.admin.bean.Cluster;
import wang.yeting.wtp.admin.bean.WtpRegistry;
import wang.yeting.wtp.admin.exceptions.PermissionException;
import wang.yeting.wtp.admin.mapper.ClusterMapper;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.ResultCode;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.dto.ClusterDto;
import wang.yeting.wtp.admin.model.enums.PermissionEnum;
import wang.yeting.wtp.admin.model.vo.ClusterVo;
import wang.yeting.wtp.admin.service.AppService;
import wang.yeting.wtp.admin.service.ClusterService;
import wang.yeting.wtp.admin.service.WtpRegistryService;
import wang.yeting.wtp.admin.util.TokenUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:44
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClusterServiceImpl extends ServiceImpl<ClusterMapper, Cluster> implements ClusterService {

    private final AppService appService;

    private final WtpRegistryService wtpRegistryService;

    private final TokenUtils tokenUtils;

    @SneakyThrows
    @Override
    public PageResponse options(ClusterVo ClusterVo, UserBo userBo) {
        if (!tokenUtils.checkAppPermission(ClusterVo.getAppId(), PermissionEnum.SELECT.getPermission(), userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        LambdaQueryWrapper<Cluster> lambdaQueryWrapper = new LambdaQueryWrapper<Cluster>().eq(Cluster::getAppId, ClusterVo.getAppId());
        lambdaQueryWrapper.select(Cluster::getAppId, Cluster::getClusterId, Cluster::getClusterName);
        List<Cluster> clusterList = list(lambdaQueryWrapper);
        return new PageResponse().setList(clusterList);
    }

    @SneakyThrows
    @Override
    public Boolean create(Cluster cluster, UserBo userBo) {
        if (!tokenUtils.checkAppAdmin(cluster.getAppId(), userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        int count = count(new LambdaQueryWrapper<Cluster>().eq(Cluster::getAppId, cluster.getAppId()).eq(Cluster::getClusterId, cluster.getClusterId()));
        if (count > 0) {
            throw new PermissionException("Cluster 已经存在");
        }
        return save(cluster);
    }

    @SneakyThrows
    @Override
    public Boolean update(Cluster cluster, UserBo userBo) {
        if (!tokenUtils.checkAppAdmin(cluster.getAppId(), userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        return updateById(cluster);
    }

    @SneakyThrows
    @Override
    public PageResponse page(ClusterVo clusterVo, UserBo userBo) {
        if (!tokenUtils.checkAppPermission(clusterVo.getAppId(), PermissionEnum.SELECT.getPermission(), userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        LambdaQueryWrapper<Cluster> lambdaQueryWrapper = new LambdaQueryWrapper<Cluster>().eq(Cluster::getAppId, clusterVo.getAppId());
        IPage<Cluster> clusterIPage = page(new Page<>(clusterVo.getPage(), clusterVo.getSize()), lambdaQueryWrapper);
        List<Cluster> clusterList = clusterIPage.getRecords();
        List<ClusterDto> clusterDtoList = clusterList.stream().map(cluster -> {
            ClusterDto clusterDto = new ClusterDto();
            BeanUtils.copyProperties(cluster, clusterDto);
            List<WtpRegistry> wtpRegistryList = wtpRegistryService.findRegistry(cluster.getAppId(), cluster.getClusterId());
            clusterDto.setWtpRegistryList(wtpRegistryList);
            return clusterDto;
        }).collect(Collectors.toList());
        return new PageResponse().setList(clusterDtoList).setPage(clusterIPage.getPages()).setTotal(clusterIPage.getTotal());
    }

    @SneakyThrows
    @Override
    public Cluster get(ClusterVo clusterVo, UserBo userBo) {
        Cluster cluster = getById(clusterVo.getId());
        if (!tokenUtils.checkAppPermission(cluster.getAppId(), PermissionEnum.SELECT.getPermission(), userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        return cluster;
    }

}
