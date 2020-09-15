package wang.yeting.wtp.admin.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author : weipeng
 * @date : 2020-07-27 20:08
 */

@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    private T data;

    public static <T> Result<T> success(T data) {
        return new Result().setCode(ResultCode.success.code).setMessage(ResultCode.success.message).setData(data);
    }

    public static Result<?> success() {
        return new Result().setCode(ResultCode.success.code).setMessage(ResultCode.success.message).setData(null);
    }

    public static Result<?> error(int code, String message) {
        return new Result().setCode(code).setMessage(message).setData(null);
    }

    public static Result<?> parameterError(String message) {
        return new Result().setCode(ResultCode.parameter_error.code).setMessage(message).setData(null);
    }

    public static Result<?> parameterError() {
        return new Result().setCode(ResultCode.parameter_error.code).setMessage(ResultCode.parameter_error.message).setData(null);
    }

    public static Result<?> noPermission(String message) {
        return new Result().setCode(ResultCode.no_permission.code).setMessage(message).setData(null);
    }

    public static Result<?> noPermission() {
        return new Result().setCode(ResultCode.no_permission.code).setMessage(ResultCode.no_permission.message).setData(null);
    }

    public static Result<?> tokenExpired(String message) {
        return new Result().setCode(ResultCode.token_expired.code).setMessage(message).setData(null);
    }

    public static Result<?> tokenExpired() {
        return new Result().setCode(ResultCode.token_expired.code).setMessage(ResultCode.token_expired.message).setData(null);
    }

    public static Result<?> exceptionError(String message) {
        return new Result().setCode(ResultCode.exception.code).setMessage(message).setData(null);
    }

    public static Result<?> exceptionError() {
        return new Result().setCode(ResultCode.exception.code).setMessage(ResultCode.exception.message).setData(null);
    }

    public boolean isSuccess() {
        return ResultCode.success.code == this.code;
    }
}
