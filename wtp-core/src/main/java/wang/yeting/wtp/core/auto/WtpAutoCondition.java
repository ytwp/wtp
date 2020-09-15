package wang.yeting.wtp.core.auto;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import wang.yeting.wtp.core.enums.WtpPropertyEnum;

/**
 * @author : weipeng
 * @date : 2020-08-25 13:32
 */

public class WtpAutoCondition implements Condition {
    /**
     * Determine if the condition matches.
     *
     * @param context  the condition context
     * @param metadata metadata of the {@link AnnotationMetadata class}
     *                 or {@link MethodMetadata method} being checked
     * @return {@code true} if the condition matches and the component can be registered,
     * or {@code false} to veto the annotated component's registration
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        Boolean property = environment.getProperty(WtpPropertyEnum.WTP_ENABLED.getProperty(), Boolean.class);
        return property == null ? false : property;
    }
}
