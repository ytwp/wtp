package wang.yeting.wtp.core.handler.impl;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.core.annotation.Wtp;
import wang.yeting.wtp.core.concurrent.WtpThreadPoolExecutor;
import wang.yeting.wtp.core.handler.WtpHandler;

import java.lang.reflect.Method;

/**
 * @author : weipeng
 * @date : 2020-07-23 15:13
 */

@Slf4j
public class MethodWtpHandler extends WtpHandler {

    private final Object target;
    private final Method method;

    public MethodWtpHandler(Object target, Method method, Wtp wtp) {
        this.target = target;
        this.method = method;
        this.wtp = wtp;
    }

    @Override
    public Boolean assignment(WtpThreadPoolExecutor threadPool) {
        try {
            method.invoke(target, threadPool);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
