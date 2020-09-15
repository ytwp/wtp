package wang.yeting.wtp.core.biz.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-08-14 16:02
 */
@Data
@Accessors(chain = true)
public class QueryBo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appId;
    private String clusterId;
    private String ip;
}
