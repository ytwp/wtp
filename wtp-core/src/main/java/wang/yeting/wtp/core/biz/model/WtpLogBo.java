package wang.yeting.wtp.core.biz.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-24 19:21
 */

@Data
@Accessors(chain = true)
public class WtpLogBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appId;
    private String clusterId;
    private String name;
    private String ip;

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

}
