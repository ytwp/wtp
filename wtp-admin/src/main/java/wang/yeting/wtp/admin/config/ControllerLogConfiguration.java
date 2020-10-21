package wang.yeting.wtp.admin.config;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author : weipeng
 * @date : 2020-10-19 5:38 下午
 */

@SpringBootConfiguration
@Slf4j
@Aspect
public class ControllerLogConfiguration {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPoint() {
    }

    @Around("restControllerPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        if (!log.isInfoEnabled()) {
            return pjp.proceed();
        }

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(requestAttributes).getRequest();

        Object result = null;
        try {
            result = pjp.proceed();
        } finally {
            stopWatch.stop();
            long costTime = stopWatch.getTotalTimeMillis();

            log.info("[{}]->[{}],time={}ms,request={},response={}",
                    request.getRemoteAddr(),
                    request.getRequestURL(),
                    costTime,
                    StrUtil.sub(JSONUtil.toJsonStr(pjp.getArgs()), 0, 300),
                    StrUtil.sub(JSONUtil.toJsonStr(result), 0, 100));
        }

        return result;
    }
}
