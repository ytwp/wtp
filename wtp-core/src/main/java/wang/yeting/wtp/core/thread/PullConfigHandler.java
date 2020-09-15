package wang.yeting.wtp.core.thread;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.core.biz.client.AdminBiz;
import wang.yeting.wtp.core.biz.model.ConfigChangeEvent;
import wang.yeting.wtp.core.biz.model.WtpConfigBean;
import wang.yeting.wtp.core.factory.WtpThreadPoolFactory;
import wang.yeting.wtp.core.util.HttpResponse;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author : weipeng
 * @date : 2020-07-24 17:28
 */

@Slf4j
public class PullConfigHandler {

    public void pullConfig(List<AdminBiz> adminBizList) {
        for (AdminBiz adminBiz : adminBizList) {
            try {
                ThreadPool.execute(() -> {
                            doPullConfig(adminBiz);
                        }
                );
            } catch (Exception e) {
                log.error("wtp ------> pullConfig error for admin[" + adminBiz + "].", e);
            }
        }
    }

    public void doPullConfig(AdminBiz adminBiz) {
        WtpConfigBean wtpConfigBean = adminBiz.getWtpConfigBean();
        Long secondsDelay = wtpConfigBean.getConnectRetryInterval();
        while (true) {
            try {
                HttpResponse<ConfigChangeEvent> response = adminBiz.pullConfig();
                if (response.getStatusCode() == HttpResponse.SUCCESS_CODE) {
                    ConfigChangeEvent configChangeEvent = response.getBody();
                    if (configChangeEvent.isChanged()) {
                        continue;
                    }
                    log.info("wtp ------> doPullConfig- configChangeEvent：[{}]", configChangeEvent);
                    changeConfig(configChangeEvent);
                } else {
                    log.error("wtp ------> doPullConfig failed. ");
                    Thread.sleep(secondsDelay);
                }
            } catch (Exception e) {
                log.error("wtp ------> doPullConfig Exception = [{}]. ", e);
                try {
                    Thread.sleep(secondsDelay);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
    }

    private Consumer<Throwable> getThrowableConsumer(AdminBiz adminBiz, Integer secondsDelay) {
        return (throwable) -> {
            log.error("wtp ------> A {} has occurred and the connection will be retried after {} seconds.", throwable.getMessage(), secondsDelay);
            doPullConfig(adminBiz);
        };
    }

    public void changeConfig(ConfigChangeEvent configChangeEvent) {
        WtpThreadPoolFactory wtpThreadPoolFactory = WtpThreadPoolFactory.getInstance();
        wtpThreadPoolFactory.changeService(configChangeEvent);
    }
}
