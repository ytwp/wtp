package wang.yeting.wtp.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import wang.yeting.wtp.admin.bean.WtpRegistry;
import wang.yeting.wtp.admin.mapper.WtpRegistryMapper;
import wang.yeting.wtp.admin.service.WtpRegistryService;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-28 21:55
 */
@Service
public class WtpRegistryServiceImpl extends ServiceImpl<WtpRegistryMapper, WtpRegistry> implements WtpRegistryService {

    @Override
    public Boolean registry(WtpRegistry wtpRegistry) {
        LambdaQueryWrapper<WtpRegistry> lambdaQueryWrapper = new LambdaQueryWrapper<WtpRegistry>()
                .eq(WtpRegistry::getAppId, wtpRegistry.getAppId())
                .eq(WtpRegistry::getClusterId, wtpRegistry.getClusterId())
                .eq(WtpRegistry::getIp, wtpRegistry.getIp());
        int count = count(lambdaQueryWrapper);
        if (count <= 0) {
            wtpRegistry.setLastPullTime(System.currentTimeMillis());
            return save(wtpRegistry);
        }
        return update(new WtpRegistry().setLastPullTime(System.currentTimeMillis()), lambdaQueryWrapper);
    }

    @Override
    public List<WtpRegistry> findByLastPullConfigTime(long lastPullConfigTime) {
        LambdaQueryWrapper<WtpRegistry> lambdaQueryWrapper = new LambdaQueryWrapper<WtpRegistry>()
                .le(WtpRegistry::getLastPullTime, lastPullConfigTime);
        return list(lambdaQueryWrapper);
    }

    @Override
    public void removeRegistryById(Integer wtpRegistryId) {
        removeById(wtpRegistryId);
    }

    @Override
    public List<WtpRegistry> findRegistry(String appId, String clusterId) {
        LambdaQueryWrapper<WtpRegistry> lambdaQueryWrapper = new LambdaQueryWrapper<WtpRegistry>()
                .eq(WtpRegistry::getAppId, appId)
                .eq(WtpRegistry::getClusterId, clusterId);
        return list(lambdaQueryWrapper);
    }

    @Override
    public Boolean remove(String appId, String clusterId, String ip) {
        LambdaQueryWrapper<WtpRegistry> lambdaQueryWrapper = new LambdaQueryWrapper<WtpRegistry>()
                .eq(WtpRegistry::getAppId, appId)
                .eq(WtpRegistry::getClusterId, clusterId)
                .eq(WtpRegistry::getIp, ip);
        return remove(lambdaQueryWrapper);
    }
}
