package wang.yeting.wtp.core.biz.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import wang.yeting.wtp.core.enums.WtpPropertyEnum;
import wang.yeting.wtp.core.exceptions.WtpConfigException;
import wang.yeting.wtp.core.util.IpUtil;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author : weipeng
 * @date : 2020-07-23 17:56
 */

@Data
@Accessors(chain = true)
public class WtpConfigBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String adminUrls;
    private String appId;
    private String clusterId;
    private String ip;
    private Long connectRetryInterval;

    public WtpConfigBean(Environment environment) {
        String adminUrls = environment.getProperty(WtpPropertyEnum.WTP_ADMIN_URL.getProperty());
        if (StringUtils.isEmpty(adminUrls)) {
            throw new WtpConfigException("wtp config --> adminAddresses  No configuration.");
        }
        this.adminUrls = adminUrls;

        String appId = environment.getProperty(WtpPropertyEnum.WTP_APP_ID.getProperty());
        if (StringUtils.isEmpty(appId)) {
            throw new WtpConfigException("wtp config --> appId  No configuration.");
        }
        this.appId = appId;

        String clusterId = environment.getProperty(WtpPropertyEnum.WTP_CLUSTER_ID.getProperty());
        if (StringUtils.isEmpty(clusterId)) {
            throw new WtpConfigException("wtp config --> clusterId  No configuration.");
        }
        this.clusterId = clusterId;

        String ip = environment.getProperty(WtpPropertyEnum.WTP_IP.getProperty());
        if (StringUtils.isEmpty(ip)) {
            ip = IpUtil.getIp();
        }
        this.ip = ip;

        Long wtpConnectRetryInterval = environment.getProperty(WtpPropertyEnum.WTP_CONNECT_RETRY_INTERVAL.getProperty(), Long.class);
        if (Objects.isNull(wtpConnectRetryInterval)) {
            wtpConnectRetryInterval = 3000L;
        }
        this.connectRetryInterval = wtpConnectRetryInterval;
    }
}
