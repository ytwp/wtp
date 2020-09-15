package wang.yeting.wtp.admin.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-27 19:20
 */
@Data
@Accessors(chain = true)
public class PageResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long total;

    private Long page;

    private List<T> list;

    private Object data;
}
