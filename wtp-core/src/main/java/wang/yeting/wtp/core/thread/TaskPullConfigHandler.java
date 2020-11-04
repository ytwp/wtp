package wang.yeting.wtp.core.thread;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.core.biz.client.AdminBiz;
import wang.yeting.wtp.core.biz.model.ConfigEvent;
import wang.yeting.wtp.core.factory.WtpThreadPoolFactory;
import wang.yeting.wtp.core.util.HttpResponse;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author : weipeng
 * @date : 2020-07-24 17:28
 */

@Slf4j
public class TaskPullConfigHandler {

    private static ScheduledFuture<?> scheduledFuture;

    public void taskPullConfig(List<AdminBiz> adminBizList) {
        scheduledFuture = ThreadPool.getScheduledThreadPoolExecutor().scheduleWithFixedDelay(() -> {
            try {
                doTaskPullConfig(adminBizList);
            } catch (Exception e) {
                log.error("wtp ------> taskPullConfig Exception .", e);
            }
        }, 300, 300, TimeUnit.SECONDS);
    }

    public void doTaskPullConfig(List<AdminBiz> adminBizList) {
        for (AdminBiz adminBiz : adminBizList) {
            try {
                HttpResponse<ConfigEvent> response = adminBiz.taskPullConfig();
                if (response.getStatusCode() == HttpResponse.SUCCESS_CODE) {
                    ConfigEvent configEvent = response.getBody();
                    refreshConfig(configEvent);
                    return;
                }
            } catch (Exception e) {
                log.error("wtp ------> doTaskPullConfig Exception .", e);
            }
        }
        log.error("wtp ------> doTaskPullConfig all AdminBiz failed. ");
    }

    public void refreshConfig(ConfigEvent configEvent) {
        WtpThreadPoolFactory wtpThreadPoolFactory = WtpThreadPoolFactory.getInstance();
        wtpThreadPoolFactory.refreshConfig(configEvent);
    }

    public static void destroy() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }
}
