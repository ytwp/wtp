package wang.yeting.wtp.admin.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-27 11:41
 */

@Data
@Accessors(chain = true)
public class Wtp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
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

    @TableLogic
    private Boolean isDeleted;
    @TableField(fill = FieldFill.INSERT)
    private Long created;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long modified;
}
