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
public class WtpLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer wtpLogId;

    private String appId;
    private String clusterId;
    private String ip;

    private String name;
    private Integer corePoolSize;
    private Integer maximumPoolSize;
    private Long keepAliveSeconds;
    private Integer activeCount;
    private Long completedTaskCount;
    private Integer queueSize;
    private Integer queueRemainingCapacity;

    private Integer largestPoolSize;
    private Integer rejectedExecutionCount;
    private Integer poolSize;
    private Long taskCount;

    private Long totalTime;
    private Long maximumTime;

    private Long logTime;

    @TableLogic
    private Boolean isDeleted;
    @TableField(fill = FieldFill.INSERT)
    private Long created;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long modified;
}
