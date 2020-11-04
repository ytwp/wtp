package wang.yeting.wtp.core.biz.client;

import wang.yeting.wtp.core.biz.model.*;
import wang.yeting.wtp.core.util.HttpResponse;

/**
 * @author : weipeng
 * @date : 2020-07-23 18:12
 */

public interface AdminBiz {

    /**
     * 获取 WtpConfigBean 配置
     *
     * @return {@link WtpConfigBean}
     */
    WtpConfigBean getWtpConfigBean();

    /**
     * 拉取配置
     *
     * @return {@link HttpResponse<ConfigChangeEvent>}
     */
    HttpResponse<ConfigChangeEvent> pullConfig();

    /**
     * 去admin注册自己
     *
     * @return {@link HttpResponse<ConfigEvent>}
     */
    HttpResponse<ConfigEvent> registry();

    /**
     * 去admin注销自己
     *
     * @return {@link HttpResponse<String>}
     */
    HttpResponse<String> destroy();

    /**
     * 推送线程池日志到 admin
     *
     * @param wtpLogBo {@link WtpLogBo}
     * @return {@link HttpResponse<Boolean>}
     */
    HttpResponse<Boolean> pushLog(WtpLogBo wtpLogBo);

    /**
     * 将未在 admin 添加的线程池， 自动添加到admin
     *
     * @param wtpBo {@link WtpBo}
     * @return {@link HttpResponse<Boolean>}
     */
    HttpResponse<Boolean> registerNoConfigurationWtp(WtpBo wtpBo);

    /**
     * 定时拉取配置，保证一致性
     *
     * @return {@link HttpResponse<ConfigEvent>}
     */
    HttpResponse<ConfigEvent> taskPullConfig();
}
