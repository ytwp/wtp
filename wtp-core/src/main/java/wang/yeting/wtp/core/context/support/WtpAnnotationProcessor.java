package wang.yeting.wtp.core.context.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import wang.yeting.wtp.core.annotation.Wtp;
import wang.yeting.wtp.core.concurrent.WtpThreadPoolExecutor;
import wang.yeting.wtp.core.context.WtpAnnotationContext;
import wang.yeting.wtp.core.exceptions.WtpConfigException;
import wang.yeting.wtp.core.handler.impl.FieldWtpHandler;
import wang.yeting.wtp.core.handler.impl.MethodWtpHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @author : weipeng
 * @date : 2020-09-10 12:23
 */
@Slf4j
public class WtpAnnotationProcessor implements BeanPostProcessor, ApplicationContextAware {

    private WtpAnnotationContext wtpAnnotationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Map<Method, Wtp> annotatedMethods = null;
        try {
            annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(), (MethodIntrospector.MetadataLookup<Wtp>) method -> AnnotatedElementUtils.findMergedAnnotation(method, Wtp.class));
        } catch (RuntimeException e) {
            log.error("wtp ------> method-wtphandler resolve error for bean[" + beanName + "].", e);
        }
        if (Objects.nonNull(annotatedMethods) && !annotatedMethods.isEmpty()) {
            for (Map.Entry<Method, Wtp> methodWtpEntry : annotatedMethods.entrySet()) {
                Method method = methodWtpEntry.getKey();
                Wtp wtp = methodWtpEntry.getValue();
                if (wtp == null) {
                    continue;
                }
                String name = getName(wtp, "wtp ------> method-wtphandler name invalid, for[" + bean.getClass() + "#" + method.getName() + "] .");
                if (!(method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(WtpThreadPoolExecutor.class))) {
                    throw new WtpConfigException("wtp ------> method-wtphandler param-classtype invalid, for[" + bean.getClass() + "#" + method.getName() + "] , " +
                            "The correct method format like \" public void setThreadPoolExecutor(WtpThreadPoolExecutor threadPoolExecutor) \" .");
                }
                method.setAccessible(true);
                wtpAnnotationContext.setWtpHandler(name, new MethodWtpHandler(bean, method, wtp));
            }
        }

        Field[] declaredFields = bean.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            boolean annotationPresent = declaredField.isAnnotationPresent(Wtp.class);
            if (annotationPresent) {
                declaredField.setAccessible(true);
                Wtp wtp = declaredField.getAnnotation(Wtp.class);
                if (wtp == null) {
                    continue;
                }
                String name = getName(wtp, "wtp ------> field-wtphandler name invalid, for[" + bean.getClass() + "#" + declaredField.getName() + "] .");
                if (!declaredField.getType().isAssignableFrom(WtpThreadPoolExecutor.class)) {
                    throw new WtpConfigException("wtp ------> field-wtphandler type invalid, for[" + bean.getClass() + "#" + declaredField.getName() + "] , " +
                            "The correct method format like \" private WtpThreadPoolExecutor threadPoolExecutor; \" .");
                }
                wtpAnnotationContext.setWtpHandler(name, new FieldWtpHandler(bean, declaredField, wtp));
            }
        }
        return bean;
    }

    private String getName(Wtp wtp, String errorLog) {
        String name = wtp.value();
        if (name.trim().length() == 0) {
            throw new WtpConfigException(errorLog);
        }
        return name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.wtpAnnotationContext = applicationContext.getBean(WtpAnnotationContext.class);
    }
}
