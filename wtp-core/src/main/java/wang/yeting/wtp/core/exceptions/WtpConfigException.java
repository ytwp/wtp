package wang.yeting.wtp.core.exceptions;

/**
 * @author : weipeng
 * @date : 2020-07-23 20:27
 */

public class WtpConfigException extends RuntimeException {

    public WtpConfigException(String message) {
        super(message);
    }

    public WtpConfigException(String message, Throwable cause) {
        super(message, cause);
    }

}
