package wang.yeting.wtp.core.handler.impl;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.core.annotation.Wtp;
import wang.yeting.wtp.core.concurrent.WtpThreadPoolExecutor;
import wang.yeting.wtp.core.handler.WtpHandler;

import java.lang.reflect.Field;

/**
 * @author : weipeng
 * @date : 2020-07-23 15:13
 */

@Slf4j
public class FieldWtpHandler extends WtpHandler {

    private final Object target;
    private final Field field;

    public FieldWtpHandler(Object target, Field field, Wtp wtp) {
        this.target = target;
        this.field = field;
        this.wtp = wtp;
    }

    @Override
    public Boolean assignment(WtpThreadPoolExecutor threadPool) {
        try {
            field.set(target, threadPool);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
