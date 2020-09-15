package wang.yeting.wtp.admin.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wang.yeting.wtp.admin.exceptions.BusinessException;
import wang.yeting.wtp.admin.exceptions.LoginException;
import wang.yeting.wtp.admin.exceptions.PermissionException;
import wang.yeting.wtp.admin.model.Result;
import wang.yeting.wtp.admin.model.ResultCode;

/**
 * @author : weipeng
 * @date : 2020-08-04 16:33
 */
@Slf4j
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(value = PermissionException.class)
    public Result handlerPermissionException(Exception exception) {
        return Result.noPermission(exception.getMessage());
    }

    @ExceptionHandler(value = LoginException.class)
    public Result handlerLoginException(Exception exception) {
        return Result.tokenExpired(exception.getMessage());
    }

    @ExceptionHandler(value = BusinessException.class)
    public Result handlerBusinessException(Exception exception) {
        return Result.exceptionError(exception.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Result handlerException(Exception exception) {
        log.error("handlerException_{}", exception.getMessage());
        return Result.exceptionError(ResultCode.exception.message);
    }
}
