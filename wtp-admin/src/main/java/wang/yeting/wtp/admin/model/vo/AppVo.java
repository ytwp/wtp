package wang.yeting.wtp.admin.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-27 19:23
 */
@Data
@Accessors(chain = true)
public class AppVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer page = 1;
    private Integer size = 10;

}
