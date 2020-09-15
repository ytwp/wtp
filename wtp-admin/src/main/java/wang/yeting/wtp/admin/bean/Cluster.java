package wang.yeting.wtp.admin.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-27 11:42
 */

@Data
@Accessors(chain = true)
public class Cluster implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String appId;
    private String clusterId;

    private String clusterName;

    @TableLogic
    private Boolean isDeleted;
    @TableField(fill = FieldFill.INSERT)
    private Long created;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long modified;
}
