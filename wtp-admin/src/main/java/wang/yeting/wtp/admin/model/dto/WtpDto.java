package wang.yeting.wtp.admin.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-30 21:55
 */
@Data
@Accessors(chain = true)
public class WtpDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer wtpId;

    private String appId;
    private String clusterId;

    private String title;

    private String ownerName;
    private String alarmEmail;
    private Boolean openAlarm;
    private Integer poolAlarmThreshold;
    private Integer queueAlarmThreshold;

    private String name;
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Long keepAliveSeconds;
    private Integer queueCapacity;
    private String queueName;
    private String rejectedExecutionHandlerName;

    private Long created;

}
