package wang.yeting.wtp.admin.service;

import wang.yeting.wtp.core.biz.model.*;

/**
 * @author : weipeng
 * @date : 2020-07-28 21:47
 */

public interface ServerService {
    ConfigEvent registry(QueryBo queryBo);

    Boolean pushLog(WtpLogBo wtpLogBo);

    ConfigChangeEvent pullConfig(String appId, String clusterId, String ip);

    Boolean registerNoConfigurationWtp(WtpBo wtpBo);

    ConfigEvent taskPullConfig(String appId, String clusterId, String ip);

    Boolean destroy(QueryBo queryBo);
}
