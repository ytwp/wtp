package wang.yeting.wtp.core.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.*;
import org.springframework.lang.Nullable;
import wang.yeting.wtp.core.context.WtpAnnotationContext;
import wang.yeting.wtp.core.context.WtpAnnotationContextAware;

/**
 * @author : weipeng
 * @date : 2020-09-10 12:23
 */

public class WtpContextAwareProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    @Nullable
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof WtpAnnotationContextAware)) {
            return bean;
        }

        invokeAwareInterfaces(bean);

        return bean;
    }

    private void invokeAwareInterfaces(Object bean) {
        if (bean instanceof WtpAnnotationContextAware) {
            WtpAnnotationContext wtpAnnotationContext = this.applicationContext.getBean(WtpAnnotationContext.class);
            ((WtpAnnotationContextAware) bean).setWtpAnnotationContext(wtpAnnotationContext);
        }

    }


    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
