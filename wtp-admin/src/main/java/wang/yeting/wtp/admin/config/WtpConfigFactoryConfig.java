package wang.yeting.wtp.admin.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.factory.WtpConfigFactory;
import wang.yeting.wtp.admin.factory.WtpFactory;
import wang.yeting.wtp.admin.service.WtpService;
import wang.yeting.wtp.admin.util.RedisUtils;

import java.util.List;

/**
 * @author : weipeng
 * @date : 2020-07-29 14:21
 */
@SpringBootConfiguration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WtpConfigFactoryConfig implements ApplicationListener<ContextRefreshedEvent> {

    private final WtpService wtpService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        RedisUtils redisUtils = applicationContext.getBean(RedisUtils.class);
        WtpFactory.refreshInstance();
        WtpFactory wtpFactory = WtpFactory.getInstance();
        WtpConfigFactory.refreshInstance(redisUtils);
        WtpConfigFactory wtpConfigFactory = WtpConfigFactory.getInstance();

        List<Wtp> wtpList = wtpService.initConfigFactory();

        wtpFactory.loadWtp(wtpList);
        wtpConfigFactory.loadConfig(wtpList);
    }

}
