package wang.yeting.wtp.admin.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-28 15:37
 */
@Data
@Accessors(chain = true)
public class WtpVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer wtpId;
    private String appId;
    private String clusterId;
    private String name;

    private Integer page = 1;
    private Integer size = 10;
}
