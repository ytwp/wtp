package wang.yeting.wtp.core.enums;

import lombok.Getter;

/**
 * @author : weipeng
 * @date : 2020-08-25 13:35
 */
@Getter
public enum WtpPropertyEnum {

    /**
     * enabled wtp
     * true or false
     */
    WTP_ENABLED("wtp.enabled"),

    /**
     * wtp-admin url
     * Multiple urls can be specified, split with ,
     * http://127.0.0.1:4997,http://127.0.0.1:4998,http://127.0.0.1:4999
     */
    WTP_ADMIN_URL("wtp.admin.url"),

    /**
     * appId
     */
    WTP_APP_ID("wtp.app.id"),

    /**
     * clusterId
     */
    WTP_CLUSTER_ID("wtp.cluster.id"),

    /**
     * Register the IP address to admin.
     * Not set, default gets native IP.
     */
    WTP_IP("wtp.ip"),

    /**
     * Wait for retry time when pull configuration fails
     */
    WTP_CONNECT_RETRY_INTERVAL("wtp.connect.retry.interval");

    private String property;

    WtpPropertyEnum(String property) {
        this.property = property;
    }

}
