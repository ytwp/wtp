package wang.yeting.wtp.admin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.bean.WtpLog;
import wang.yeting.wtp.admin.bean.WtpRegistry;
import wang.yeting.wtp.admin.factory.WtpConfigFactory;
import wang.yeting.wtp.admin.handler.DeferredResultHandler;
import wang.yeting.wtp.admin.model.vo.WtpVo;
import wang.yeting.wtp.admin.service.ServerService;
import wang.yeting.wtp.admin.service.WtpLogService;
import wang.yeting.wtp.admin.service.WtpRegistryService;
import wang.yeting.wtp.admin.service.WtpService;
import wang.yeting.wtp.admin.thread.PullConfigMonitorHandler;
import wang.yeting.wtp.admin.thread.WtpLogMonitorHandler;
import wang.yeting.wtp.core.biz.model.*;
import wang.yeting.wtp.core.util.HttpResponse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author : weipeng
 * @date : 2020-07-28 21:47
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServerServiceImpl implements ServerService {

    private final WtpService wtpService;
    private final WtpLogService wtpLogService;
    private final WtpRegistryService wtpRegistryService;
    private final WtpLogMonitorHandler wtpLogMonitorHandler;

    @Value("${pull.config.hold.count:30}")
    private Integer pullConfigHoldCount;

    private static final HttpResponse<ConfigChangeEvent> DEFAULT_HTTP_RESPONSE = new HttpResponse<>(HttpResponse.SUCCESS_CODE, new ConfigChangeEvent(new ConcurrentHashMap(0)));

    @Override
    public ConfigEvent registry(QueryBo queryBo) {
        log.info("registry,{}", queryBo);
        WtpRegistry wtpRegistry = new WtpRegistry();
        BeanUtils.copyProperties(queryBo, wtpRegistry);
        wtpRegistryService.registry(wtpRegistry);

        WtpConfigFactory wtpConfigFactory = WtpConfigFactory.getInstance();
        CopyOnWriteArrayList<Config> configList = wtpConfigFactory.getConfigList(queryBo.getAppId(), queryBo.getClusterId());

        ConcurrentMap<String, Config> configConcurrentMap = configList.stream().collect(Collectors.toConcurrentMap(Config::getName, Config -> Config));
        return new ConfigEvent(configConcurrentMap);
    }

    @Override
    public Boolean destroy(QueryBo queryBo) {
        return wtpRegistryService.remove(queryBo.getAppId(), queryBo.getClusterId(), queryBo.getIp());
    }

    @Override
    public Boolean pushLog(WtpLogBo wtpLogBo) {
        wtpLogMonitorHandler.wtpLogMonitor(wtpLogBo);
        WtpLog wtpLog = new WtpLog();
        BeanUtils.copyProperties(wtpLogBo, wtpLog);
        return wtpLogService.create(wtpLog);
    }

    /**
     * @param appId
     * @param clusterId
     * @param ip
     * @return
     */
    @Override
    public DeferredResult<HttpResponse<ConfigChangeEvent>> pullConfig(String appId, String clusterId, String ip) {
        log.info("pullConfig ------> appId：{}  --  clusterId：{}", appId, clusterId);
        wtpRegistryService.registry(new WtpRegistry().setAppId(appId).setClusterId(clusterId).setIp(ip));

        DeferredResult<HttpResponse<ConfigChangeEvent>> deferredResult = new DeferredResult<>(pullConfigHoldCount * 1000L, DEFAULT_HTTP_RESPONSE);

        DeferredResultHandler DeferredResultHandler = new DeferredResultHandler(appId, clusterId, ip, pullConfigHoldCount, deferredResult);

        PullConfigMonitorHandler.add(DeferredResultHandler);

        deferredResult.onCompletion(() -> PullConfigMonitorHandler.remove(DeferredResultHandler));

        return deferredResult;
    }

    @Override
    public Boolean registerNoConfigurationWtp(WtpBo wtpBo) {
        Wtp wtpDb = wtpService.info(new WtpVo().setAppId(wtpBo.getAppId()).setClusterId(wtpBo.getClusterId()).setName(wtpBo.getName()));
        if (wtpDb != null) {
            return false;
        }
        Wtp wtp = new Wtp();
        BeanUtils.copyProperties(wtpBo, wtp);
        return wtpService.add(wtp);
    }

    @Override
    public ConfigEvent taskPullConfig(String appId, String clusterId, String ip) {
        log.info("taskPullConfig ------> appId,{}  --  clusterId,{}", appId, clusterId);
        wtpRegistryService.registry(new WtpRegistry().setAppId(appId).setClusterId(clusterId).setIp(ip));

        WtpConfigFactory wtpConfigFactory = WtpConfigFactory.getInstance();
        CopyOnWriteArrayList<Config> configList = wtpConfigFactory.getConfigList(appId, clusterId);

        ConcurrentMap<String, Config> configConcurrentMap = configList.stream().collect(Collectors.toConcurrentMap(Config::getName, Config -> Config));
        return new ConfigEvent(configConcurrentMap);
    }

}
