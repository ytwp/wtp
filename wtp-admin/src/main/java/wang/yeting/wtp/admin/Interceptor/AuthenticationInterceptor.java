package wang.yeting.wtp.admin.Interceptor;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import wang.yeting.wtp.admin.annotation.Permission;
import wang.yeting.wtp.admin.exceptions.LoginException;
import wang.yeting.wtp.admin.exceptions.PermissionException;
import wang.yeting.wtp.admin.model.ResultCode;
import wang.yeting.wtp.admin.model.bo.UserBo;
import wang.yeting.wtp.admin.util.TokenUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author : weipeng
 * @date : 2020-08-20 18:45
 */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenUtils tokenUtils;

    @SneakyThrows({LoginException.class, PermissionException.class})
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Permission permission = method.getAnnotation(Permission.class);
            Permission classPermission = method.getDeclaringClass().getAnnotation(Permission.class);
            if (permission != null || classPermission != null) {
                if (permission == null) {
                    permission = classPermission;
                }
                String token = request.getHeader("token");
                UserBo userBo = tokenUtils.getUserBoByToken(token);
                request.setAttribute("userBo", userBo);
                if (permission.login() && userBo == null) {
                    throw new LoginException(ResultCode.token_expired.message);
                }
                if (permission.roles().length > 0) {
                    boolean checkRole = tokenUtils.checkRole(permission.roles(), userBo.getRoles());
                    if (!checkRole) {
                        throw new PermissionException(ResultCode.parameter_error.message);
                    }
                }
                if (permission.permissions().length > 0) {
                    boolean checkRolePermission = tokenUtils.checkRolePermission(permission.permissions(), userBo.getPermissions());
                    if (!checkRolePermission) {
                        throw new PermissionException(ResultCode.no_permission.message);
                    }
                }
            }
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }

}
