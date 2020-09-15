package wang.yeting.wtp.admin.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-28 19:58
 */
@Data
@Accessors(chain = true)
public class WtpLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appId;
    private String clusterId;
    private String name;
    private String ip;

    private Long startTime;
    private Long endTime;

    private Integer page = 1;
    private Integer size = 10;
}
