package wang.yeting.wtp.core.biz.client;

import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.core.biz.model.*;
import wang.yeting.wtp.core.util.HttpLongPollingUtil;
import wang.yeting.wtp.core.util.HttpRequest;
import wang.yeting.wtp.core.util.HttpResponse;
import wang.yeting.wtp.core.util.HttpUtil;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collections;

/**
 * @author : weipeng
 * @date : 2020-07-23 18:04
 */

@Slf4j
public class AdminBizClient implements AdminBiz, Serializable {

    private static final long serialVersionUID = 1L;

    private HttpLongPollingUtil httpLongPollingUtil;
    private HttpRequest httpRequest;

    private static String PULL_CONFIG_URL = "/pullConfig";
    private static String TASK_PULL_CONFIG_URL = "/taskPullConfig";
    private static final String REGISTRY_URL = "/registry";
    private static final String DESTROY_URL = "/destroy";
    private static final String PUSH_LOG_URL = "/pushLog";
    private static final String REGISTER_NO_CONFIGURATION_WTP_URL = "/registerNoConfigurationWtp";

    private static final Type configChangeEventType = TypeToken.getParameterized(HttpResponse.class, ConfigChangeEvent.class).getType();
    private static final Type configEventType = TypeToken.getParameterized(HttpResponse.class, ConfigEvent.class).getType();
    private static final Type booleanType = TypeToken.getParameterized(HttpResponse.class, Boolean.class).getType();

    private WtpConfigBean wtpConfigBean;

    private QueryBo queryBo;

    private String addressUrl;

    public AdminBizClient(String addressUrl, WtpConfigBean wtpConfigBean) {
        this.wtpConfigBean = wtpConfigBean;
        this.addressUrl = addressUrl;
        this.queryBo = new QueryBo().setAppId(wtpConfigBean.getAppId()).setClusterId(wtpConfigBean.getClusterId()).setIp(wtpConfigBean.getIp());
        PULL_CONFIG_URL = PULL_CONFIG_URL + "/" + wtpConfigBean.getAppId() + "/" + wtpConfigBean.getClusterId() + "/" + wtpConfigBean.getIp();
        TASK_PULL_CONFIG_URL = TASK_PULL_CONFIG_URL + "/" + wtpConfigBean.getAppId() + "/" + wtpConfigBean.getClusterId() + "/" + wtpConfigBean.getIp();
        httpLongPollingUtil = new HttpLongPollingUtil();
        httpRequest = new HttpRequest().setUrl(this.addressUrl + PULL_CONFIG_URL).setConnectTimeout(30000).setReadTimeout(60000);
    }

    @Override
    public WtpConfigBean getWtpConfigBean() {
        return this.wtpConfigBean;
    }

    @Override
    public HttpResponse<ConfigChangeEvent> pullConfig() {
        try {
            return httpLongPollingUtil.doGet(httpRequest, configChangeEventType);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return new HttpResponse<>(HttpResponse.FAIL_CODE, null);
    }

    @Override
    public HttpResponse<ConfigEvent> registry() {
        try {
            return HttpUtil.postJson(this.addressUrl + REGISTRY_URL, queryBo, configEventType);
        } catch (Exception e) {
            return new HttpResponse<>(HttpResponse.FAIL_CODE, null);
        }
    }

    @Override
    public HttpResponse<String> destroy() {
        try {
            return HttpUtil.postJson(this.addressUrl + DESTROY_URL, queryBo, booleanType);
        } catch (Exception e) {
            return new HttpResponse<>(HttpResponse.FAIL_CODE, null);
        }
    }

    @Override
    public HttpResponse<Boolean> pushLog(WtpLogBo wtpLogBo) {
        try {
            return HttpUtil.postJson(this.addressUrl + PUSH_LOG_URL, wtpLogBo, booleanType);
        } catch (Exception e) {
            return new HttpResponse<>(HttpResponse.FAIL_CODE, null);
        }
    }

    @Override
    public HttpResponse<Boolean> registerNoConfigurationWtp(WtpBo wtpBo) {
        try {
            return HttpUtil.postJson(this.addressUrl + REGISTER_NO_CONFIGURATION_WTP_URL, wtpBo, booleanType);
        } catch (Exception e) {
            return new HttpResponse<>(HttpResponse.FAIL_CODE, null);
        }
    }

    @Override
    public HttpResponse<ConfigEvent> taskPullConfig() {
        try {
            return HttpUtil.postJson(this.addressUrl + TASK_PULL_CONFIG_URL, Collections.emptyMap(), configEventType);
        } catch (Exception e) {
            return new HttpResponse<>(HttpResponse.FAIL_CODE, null);
        }
    }
}
