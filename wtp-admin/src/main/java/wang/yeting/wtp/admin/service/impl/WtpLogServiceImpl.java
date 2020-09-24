package wang.yeting.wtp.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.yeting.wtp.admin.bean.WtpLog;
import wang.yeting.wtp.admin.mapper.WtpLogMapper;
import wang.yeting.wtp.admin.model.PageResponse;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.model.enums.PermissionEnum;
import wang.yeting.wtp.admin.model.vo.WtpLogVo;
import wang.yeting.wtp.admin.service.WtpLogService;
import wang.yeting.wtp.admin.util.TokenUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:48
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WtpLogServiceImpl extends ServiceImpl<WtpLogMapper, WtpLog> implements WtpLogService {

    private final TokenUtils tokenUtils;

    @Override
    public WtpLog realTime(WtpLogVo wtpLogVo) {
        LambdaQueryWrapper<WtpLog> lambdaQueryWrapper = new LambdaQueryWrapper<WtpLog>()
                .eq(WtpLog::getClusterId, wtpLogVo.getClusterId())
                .eq(WtpLog::getName, wtpLogVo.getName())
                .eq(WtpLog::getAppId, wtpLogVo.getAppId())
                .eq(StringUtils.isNotBlank(wtpLogVo.getIp()), WtpLog::getIp, wtpLogVo.getIp());
        lambdaQueryWrapper.orderByDesc(WtpLog::getLogTime);
        IPage<WtpLog> page = page(new Page<>(1, 1), lambdaQueryWrapper);
        List<WtpLog> wtpLogList = page.getRecords();
        if (CollectionUtil.isNotEmpty(wtpLogList)) {
            return wtpLogList.get(0);
        }
        return null;
    }

    @Override
    public Boolean create(WtpLog wtpLog) {
        return save(wtpLog);
    }

    @Override
    public List<WtpLog> chart(WtpLogVo wtpLogVo) {
        Long startTime = wtpLogVo.getStartTime();
        Long endTime = wtpLogVo.getEndTime();
        if (endTime == null) {
            endTime = System.currentTimeMillis();
        }
        if (startTime == null) {
            startTime = endTime - 1800000L;
        }
        LambdaQueryWrapper<WtpLog> lambdaQueryWrapper = new LambdaQueryWrapper<WtpLog>()
                .eq(WtpLog::getAppId, wtpLogVo.getAppId())
                .eq(WtpLog::getClusterId, wtpLogVo.getClusterId())
                .eq(WtpLog::getName, wtpLogVo.getName())
                .eq(StringUtils.isNotBlank(wtpLogVo.getIp()), WtpLog::getIp, wtpLogVo.getIp())
                .ge(WtpLog::getLogTime, startTime)
                .le(WtpLog::getLogTime, endTime)
                .orderByAsc(WtpLog::getLogTime);
        return list(lambdaQueryWrapper);
    }

    @Override
    public PageResponse<WtpLog> page(WtpLogVo wtpLogVo, UserBo userBo) {
        LambdaQueryWrapper<WtpLog> lambdaQueryWrapper = new LambdaQueryWrapper<WtpLog>()
                .eq(StringUtils.isNotBlank(wtpLogVo.getAppId()), WtpLog::getAppId, wtpLogVo.getAppId())
                .eq(StringUtils.isNotBlank(wtpLogVo.getClusterId()), WtpLog::getClusterId, wtpLogVo.getClusterId())
                .eq(StringUtils.isNotBlank(wtpLogVo.getName()), WtpLog::getName, wtpLogVo.getName())
                .eq(StringUtils.isNotBlank(wtpLogVo.getIp()), WtpLog::getIp, wtpLogVo.getIp())
                .ge(Objects.nonNull(wtpLogVo.getStartTime()), WtpLog::getLogTime, wtpLogVo.getStartTime())
                .le(Objects.nonNull(wtpLogVo.getEndTime()), WtpLog::getLogTime, wtpLogVo.getEndTime())
                .orderByDesc(WtpLog::getLogTime);
        if (!tokenUtils.checkSuperAdmin(userBo)) {
            List<String> permissions = userBo.getPermissions();
            Set<String> appIdSet = new HashSet<>();
            for (String permission : permissions) {
                if (permission.contains(PermissionEnum.ADMIN.getPermission()) || permission.contains(PermissionEnum.SELECT.getPermission())) {
                    String[] split = StringUtils.split(permission, TokenUtils.PERMISSION_SPACE_MARK);
                    appIdSet.add(split[0]);
                }
            }
            lambdaQueryWrapper.in(WtpLog::getAppId, appIdSet);
        }
        IPage<WtpLog> page = page(new Page<>(wtpLogVo.getPage(), wtpLogVo.getSize()), lambdaQueryWrapper);
        return new PageResponse<WtpLog>().setList(page.getRecords()).setPage(page.getPages()).setTotal(page.getTotal());
    }
}
