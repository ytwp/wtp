package wang.yeting.wtp.admin.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-08-22 13:30
 */
@Data
@Accessors(chain = true)
public class UserAppPermissionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer userId;
    private String appId;

    private String id;
    private String label;

    List<UserAppPermissionDto> children;
}
