package wang.yeting.wtp.core.util;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-23 20:16
 */
@Data
@Accessors(chain = true)
public class HttpResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    private int statusCode;
    private T body;

    public HttpResponse(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public HttpResponse() {
    }
}
