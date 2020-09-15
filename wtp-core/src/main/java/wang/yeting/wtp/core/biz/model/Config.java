package wang.yeting.wtp.core.biz.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-23 20:59
 */
@Data
@Accessors(chain = true)
public class Config implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer wtpId;

    private String appId;

    private String clusterId;

    private String name;

    private Integer corePoolSize;

    private Integer maximumPoolSize;

    private Long keepAliveSeconds;

    private Integer queueCapacity;

    private String queueName;

    private String rejectedExecutionHandlerName;

}
