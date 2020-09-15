package wang.yeting.wtp.admin.service;

import wang.yeting.wtp.admin.bean.WtpRegistry;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-28 21:55
 */

public interface WtpRegistryService {
    Boolean registry(WtpRegistry wtpRegistry);

    List<WtpRegistry> findByLastPullConfigTime(long lastPullConfigTime);

    void removeRegistryById(Integer wtpRegistryId);

    List<WtpRegistry> findRegistry(String appId, String clusterId);

    Boolean remove(String appId, String clusterId, String ip);
}
