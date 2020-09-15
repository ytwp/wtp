package wang.yeting.wtp.core.exceptions;

/**
 * @author : weipeng
 * @date : 2020-07-23 20:26
 */

public class WtpConfigStatusCodeException extends RuntimeException{

    private final int statusCode;

    public WtpConfigStatusCodeException(int statusCode, String message) {
        super(String.format("[status code: %d] %s", statusCode, message));
        this.statusCode = statusCode;
    }

    public WtpConfigStatusCodeException(int statusCode, Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
