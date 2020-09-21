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

    /**
     * 线程池名称
     * Thread pool name
     */
    private String name;

    /**
     * 核心线程数
     * Number of core threads
     */
    private Integer corePoolSize;

    /**
     * 最大线程数
     * Maximum number of threads
     */
    private Integer maximumPoolSize;

    /**
     * 回收线程时间(秒)
     * Thread recovery time(second )
     */
    private Long keepAliveSeconds;

    /**
     * 活跃线程数
     * Number of active threads
     */
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
