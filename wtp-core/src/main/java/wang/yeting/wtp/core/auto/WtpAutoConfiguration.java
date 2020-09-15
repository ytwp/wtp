package wang.yeting.wtp.core.auto;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import wang.yeting.wtp.core.biz.model.WtpConfigBean;
import wang.yeting.wtp.core.spi.WtpPropertyProcessor;

/**
 * @author : weipeng
 * @date : 2020-08-25 13:25
 */
@Configuration
@Conditional(WtpAutoCondition.class)
@Import(WtpRegistrar.class)
public class WtpAutoConfiguration implements EnvironmentAware {

    private Environment environment;

    @Bean
    public WtpPropertyProcessor wtpPropertyProcessor() {
        WtpConfigBean wtpConfigBean = new WtpConfigBean(environment);
        return new WtpPropertyProcessor(wtpConfigBean);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
