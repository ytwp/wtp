package wang.yeting.wtp.admin.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import wang.yeting.wtp.admin.bean.WtpRegistry;

import java.io.Serializable;
import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-27 11:42
 */

@Data
@Accessors(chain = true)
public class ClusterDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String appId;
    private String clusterId;

    private String clusterName;

    private Boolean isDeleted;
    private Long created;
    private Long modified;

    private List<WtpRegistry> wtpRegistryList;
}
