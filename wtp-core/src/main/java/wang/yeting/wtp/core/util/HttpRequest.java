package wang.yeting.wtp.core.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * @author : weipeng
 * @date : 2020-07-23 20:16
 */

@Data
@Accessors(chain = true)
public class HttpRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String url;
    private Map<String, String> headers;
    private int connectTimeout;
    private int readTimeout;

}
