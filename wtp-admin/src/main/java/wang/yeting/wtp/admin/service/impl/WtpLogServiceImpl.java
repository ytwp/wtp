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
import wang.yeting.wtp.admin.model.vo.WtpLogVo;
import wang.yeting.wtp.admin.service.WtpLogService;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-27 15:48
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WtpLogServiceImpl extends ServiceImpl<WtpLogMapper, WtpLog> implements WtpLogService {
    @Override
    public WtpLog realTime(WtpLogVo WtpLogVo) {
        LambdaQueryWrapper<WtpLog> lambdaQueryWrapper = new LambdaQueryWrapper<WtpLog>()
                .eq(WtpLog::getClusterId, WtpLogVo.getClusterId())
                .eq(WtpLog::getName, WtpLogVo.getName())
                .eq(WtpLog::getAppId, WtpLogVo.getAppId())
                .eq(StringUtils.isNotBlank(WtpLogVo.getIp()), WtpLog::getIp, WtpLogVo.getIp());
        lambdaQueryWrapper.orderByDesc(WtpLog::getLogTime);
        IPage<WtpLog> clusterIPage = page(new Page<>(1, 1), lambdaQueryWrapper);
        List<WtpLog> wtpLogList = clusterIPage.getRecords();
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
    public List<WtpLog> chart(WtpLogVo WtpLogVo) {
        Long startTime = WtpLogVo.getStartTime();
        Long endTime = WtpLogVo.getEndTime();
        if (endTime == null) {
            endTime = System.currentTimeMillis();
        }
        if (startTime == null) {
            startTime = endTime - 1800000L;
        }
        LambdaQueryWrapper<WtpLog> lambdaQueryWrapper = new LambdaQueryWrapper<WtpLog>()
                .eq(WtpLog::getAppId, WtpLogVo.getAppId())
                .eq(WtpLog::getClusterId, WtpLogVo.getClusterId())
                .eq(WtpLog::getName, WtpLogVo.getName())
                .eq(StringUtils.isNotBlank(WtpLogVo.getIp()), WtpLog::getIp, WtpLogVo.getIp())
                .ge(WtpLog::getLogTime, startTime)
                .le(WtpLog::getLogTime, endTime);
        lambdaQueryWrapper.orderByAsc(WtpLog::getLogTime);
        return list(lambdaQueryWrapper);
    }
}
