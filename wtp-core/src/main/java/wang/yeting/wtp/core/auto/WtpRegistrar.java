package wang.yeting.wtp.core.auto;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import wang.yeting.wtp.core.context.support.AbstractWtpAnnotationContext;
import wang.yeting.wtp.core.context.support.WtpAnnotationProcessor;
import wang.yeting.wtp.core.context.support.WtpContextAwareProcessor;

/**
 * @author : weipeng
 * @date : 2020-09-10 12:43
 */

public class WtpRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String WTP_CONTEXT_AWARE_PROCESSOR_NAME = "wang.yeting.wtp.core.context.support.wtpContextAwareProcessor";

    private static final String WTP_ANNOTATION_PROCESSOR_NAME = "wang.yeting.wtp.core.context.support.wtpAnnotationProcessor";

    private static final String ABSTRACT_WTP_ANNOTATION_CONTEXT_NAME = "wang.yeting.wtp.core.context.support.abstractWtpAnnotationContext";

    /**
     * Register bean definitions as necessary based on the given annotation metadata of
     * the importing {@code @Configuration} class.
     * <p>Note that {@link BeanDefinitionRegistryPostProcessor} types may <em>not</em> be
     * registered here, due to lifecycle constraints related to {@code @Configuration}
     * class processing.
     * <p>The default implementation is empty.
     *
     * @param importingClassMetadata annotation metadata of the importing class
     * @param registry               current bean definition registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        RootBeanDefinition wtpAnnotationContextRootBeanDefinition = new RootBeanDefinition(AbstractWtpAnnotationContext.class);
        registry.registerBeanDefinition(ABSTRACT_WTP_ANNOTATION_CONTEXT_NAME, wtpAnnotationContextRootBeanDefinition);

        RootBeanDefinition wtpAnnotationProcessorRootBeanDefinition = new RootBeanDefinition(WtpAnnotationProcessor.class);
        registry.registerBeanDefinition(WTP_ANNOTATION_PROCESSOR_NAME, wtpAnnotationProcessorRootBeanDefinition);

        RootBeanDefinition wtpContextAwareProcessorRootBeanDefinition = new RootBeanDefinition(WtpContextAwareProcessor.class);
        registry.registerBeanDefinition(WTP_CONTEXT_AWARE_PROCESSOR_NAME, wtpContextAwareProcessorRootBeanDefinition);
    }
}
