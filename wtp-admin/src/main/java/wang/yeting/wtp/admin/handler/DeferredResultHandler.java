package wang.yeting.wtp.admin.handler;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.async.DeferredResult;
import wang.yeting.wtp.admin.factory.WtpConfigFactory;
import wang.yeting.wtp.admin.thread.PullConfigMonitorHandler;
import wang.yeting.wtp.core.biz.model.Config;
import wang.yeting.wtp.core.biz.model.ConfigChangeEvent;
import wang.yeting.wtp.core.util.HttpResponse;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author : weipeng
 * @date : 2020-07-30 19:56
 */
@Data
@Slf4j
public class DeferredResultHandler {

    private String appId;
    private String clusterId;
    private String ip;
    private int count;
    private DeferredResult<HttpResponse<ConfigChangeEvent>> deferredResult;

    /**
     * Monitor the configuration, return and remove this when changes occur, no change occurs, and remove this when the specified number of times is reached
     * 监控配置，当发生变化时返回并删除，不发生变化，当达到指定次数时删除
     */
    public void monitor() {
        WtpConfigFactory wtpConfigFactory = WtpConfigFactory.getInstance();
        Boolean configChange = wtpConfigFactory.isConfigChange(appId, clusterId);
        if (configChange) {
            CopyOnWriteArrayList<Config> configList = wtpConfigFactory.getConfigChange(appId, clusterId, ip);
            ConcurrentMap<String, Config> configConcurrentMap = configList.stream().collect(Collectors.toConcurrentMap(Config::getName, Config -> Config));
            deferredResult.setResult(new HttpResponse<>(HttpResponse.SUCCESS_CODE, new ConfigChangeEvent(configConcurrentMap)));
            PullConfigMonitorHandler.remove(this);
        }
        if (--count == 0) {
            PullConfigMonitorHandler.remove(this);
        }
    }

    public DeferredResultHandler(String appId, String clusterId, String ip, int count, DeferredResult<HttpResponse<ConfigChangeEvent>> deferredResult) {
        this.appId = appId;
        this.clusterId = clusterId;
        this.ip = ip;
        this.count = count;
        this.deferredResult = deferredResult;
    }

    public DeferredResultHandler() {
    }
}
