package wang.yeting.wtp.admin.model;

/**
 * @author : weipeng
 * @date : 2020-07-27 20:58
 */

public enum ResultCode {

    success(20000, "Successful. "),
    parameter_error(50000, "Parameters of the abnormal. "),
    exception(50001, "System is abnormal. Please try again later. "),
    token_expired(50014, "Login invalid, please login again. "),
    illegal_token(50008, "Illegal token. "),
    other_clients_logged_in(50012, "Other clients logged in. "),
    no_permission(50010, "Unauthorized operation. ");

    public int code;
    public String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
