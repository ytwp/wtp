package wang.yeting.wtp.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.bean.WtpRegistry;
import wang.yeting.wtp.admin.exceptions.BusinessException;
import wang.yeting.wtp.admin.exceptions.PermissionException;
import wang.yeting.wtp.admin.factory.WtpConfigFactory;
import wang.yeting.wtp.admin.mapper.WtpMapper;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.ResultCode;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.dto.WtpDto;
import wang.yeting.wtp.admin.model.enums.PermissionEnum;
import wang.yeting.wtp.admin.model.vo.WtpVo;
import wang.yeting.wtp.admin.service.WtpRegistryService;
import wang.yeting.wtp.admin.service.WtpService;
import wang.yeting.wtp.admin.util.TokenUtils;

import java.net.URLDecoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:49
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WtpServiceImpl extends ServiceImpl<WtpMapper, Wtp> implements WtpService {

    private final WtpRegistryService wtpRegistryService;

    private final TokenUtils tokenUtils;

    @SneakyThrows
    @Override
    public Boolean create(Wtp wtp, UserBo userBo) {
        if (!tokenUtils.checkAppPermission(wtp.getAppId(), PermissionEnum.INSERT.getPermission(), userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        int count = count(new LambdaQueryWrapper<Wtp>().eq(Wtp::getAppId, wtp.getAppId()).eq(Wtp::getClusterId, wtp.getClusterId()).eq(Wtp::getName, wtp.getName()));
        if (count > 0) {
            throw new BusinessException("Wtp 已经存在");
        }
        return save(wtp);
    }

    @SneakyThrows
    @Override
    public PageResponse<WtpDto> page(WtpVo wtpVo, UserBo userBo) {
        if (!tokenUtils.checkAppPermission(wtpVo.getAppId(), PermissionEnum.SELECT.getPermission(), userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        LambdaQueryWrapper<Wtp> lambdaQueryWrapper = new LambdaQueryWrapper<Wtp>()
                .eq(Wtp::getAppId, wtpVo.getAppId())
                .eq(Wtp::getClusterId, wtpVo.getClusterId())
                .eq(StringUtils.isNotBlank(wtpVo.getName()), Wtp::getName, wtpVo.getName());
        IPage<Wtp> page = page(new Page<>(wtpVo.getPage(), wtpVo.getSize()), lambdaQueryWrapper);
        List<Wtp> wtpList = page.getRecords();
        List<WtpRegistry> wtpRegistryList = wtpRegistryService.findRegistry(wtpVo.getAppId(), wtpVo.getClusterId());
        List<WtpDto> wtpDtoList = wtpList.stream().map(wtp -> {
            WtpDto wtpDto = new WtpDto();
            BeanUtils.copyProperties(wtp, wtpDto);
            return wtpDto;
        }).collect(Collectors.toList());
        return new PageResponse<WtpDto>().setData(wtpRegistryList).setList(wtpDtoList).setPage(page.getPages()).setTotal(page.getTotal());
    }

    @SneakyThrows
    @Override
    public Boolean update(Wtp wtp, UserBo userBo) {
        if (!tokenUtils.checkAppPermission(wtp.getAppId(), PermissionEnum.UPDATE.getPermission(), userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        Wtp wtpDb = getById(wtp.getWtpId());
        List<WtpRegistry> wtpRegistryList = wtpRegistryService.findRegistry(wtpDb.getAppId(), wtpDb.getClusterId());
        WtpConfigFactory wtpConfigFactory = WtpConfigFactory.getInstance();
        Boolean change = wtpConfigFactory.change(wtp, wtpRegistryList);
        if (!change) {
            return false;
        }
        return updateById(wtp);
    }

    @SneakyThrows
    @Override
    public Wtp get(WtpVo wtpVo, UserBo userBo) {
        Wtp wtp = getById(wtpVo.getWtpId());
        if (!tokenUtils.checkAppPermission(wtp.getAppId(), PermissionEnum.SELECT.getPermission(), userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        return wtp;
    }

    @Override
    public Wtp info(WtpVo wtpVo) {
        LambdaQueryWrapper<Wtp> lambdaQueryWrapper = new LambdaQueryWrapper<Wtp>()
                .eq(Wtp::getAppId, wtpVo.getAppId())
                .eq(Wtp::getClusterId, wtpVo.getClusterId())
                .eq(Wtp::getName, wtpVo.getName());
        return getOne(lambdaQueryWrapper);
    }

    @Override
    public Boolean add(Wtp wtp) {
        return save(wtp);
    }

    @Override
    public List<Wtp> initConfigFactory() {
        return list();
    }

    @Override
    public Wtp get(LambdaQueryWrapper<Wtp> lambdaQueryWrapper) {
        return getOne(lambdaQueryWrapper);
    }

    @SneakyThrows
    @Override
    public Boolean del(Wtp wtp, UserBo userBo) {
        if (!tokenUtils.checkAppPermission(wtp.getAppId(), PermissionEnum.SELECT.getPermission(), userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        return removeById(wtp.getWtpId());
    }

    @Override
    @SneakyThrows({BusinessException.class, PermissionException.class})
    @Transactional(rollbackFor = BusinessException.class)
    public Boolean syncConfig(Wtp wtp, String clusterIds, UserBo userBo) {
        if (!tokenUtils.checkAppPermission(wtp.getAppId(), PermissionEnum.UPDATE.getPermission(), userBo)) {
            throw new PermissionException(ResultCode.no_permission.message);
        }
        wtp.setWtpId(null);
        wtp.setIsDeleted(null);
        wtp.setCreated(null);
        wtp.setModified(null);
        List<String> clusterIdList = JSON.parseArray(URLDecoder.decode(clusterIds), String.class);
        for (String clusterId : clusterIdList) {
            wtp.setClusterId(clusterId);
            LambdaQueryWrapper<Wtp> lambdaQueryWrapper = new LambdaQueryWrapper<Wtp>().eq(Wtp::getAppId, wtp.getAppId()).eq(Wtp::getClusterId, clusterId).eq(Wtp::getName, wtp.getName());
            int count = count(lambdaQueryWrapper);
            boolean sync;
            if (count == 0) {
                sync = save(wtp);
            } else {
                sync = update(wtp, lambdaQueryWrapper);
            }
            if (!sync) {
                throw new BusinessException(clusterId + "同步失败，已回滚。");
            }
        }
        return true;
    }

}
