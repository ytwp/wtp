package wang.yeting.wtp.admin.service;

import org.springframework.web.context.request.async.DeferredResult;
import wang.yeting.wtp.core.biz.model.*;
import wang.yeting.wtp.core.util.HttpResponse;

/**
 * @author : weipeng
 * @date : 2020-07-28 21:47
 */

public interface ServerService {
    ConfigEvent registry(QueryBo queryBo);

    Boolean pushLog(WtpLogBo wtpLogBo);

    DeferredResult<HttpResponse<ConfigChangeEvent>> pullConfig(String appId, String clusterId, String ip);

    Boolean registerNoConfigurationWtp(WtpBo wtpBo);

    ConfigEvent taskPullConfig(String appId, String clusterId, String ip);

    Boolean destroy(QueryBo queryBo);
}
