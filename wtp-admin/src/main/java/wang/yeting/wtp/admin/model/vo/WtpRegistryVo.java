package wang.yeting.wtp.admin.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-08-24 17:29
 */
@Data
@Accessors(chain = true)
public class WtpRegistryVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appId;
    private String clusterId;
    private String name;
    private String ip;

    private Integer page = 1;
    private Integer size = 10;
}
