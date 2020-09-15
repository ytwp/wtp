package wang.yeting.wtp.core.biz.client;

import wang.yeting.wtp.core.biz.model.*;
import wang.yeting.wtp.core.util.HttpResponse;

/**
 * @author : weipeng
 * @date : 2020-07-23 18:12
 */

public interface AdminBiz {

    WtpConfigBean getWtpConfigBean();

    HttpResponse<ConfigChangeEvent> pullConfig();

    HttpResponse<ConfigEvent> registry();

    HttpResponse<String> destroy();

    HttpResponse<Boolean> pushLog(WtpLogBo wtpLogBo);

    HttpResponse<Boolean> registerNoConfigurationWtp(WtpBo wtpBo);

    HttpResponse<ConfigEvent> taskPullConfig();
}
