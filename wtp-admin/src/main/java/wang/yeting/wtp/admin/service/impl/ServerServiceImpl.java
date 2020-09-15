package wang.yeting.wtp.admin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.bean.WtpLog;
import wang.yeting.wtp.admin.bean.WtpRegistry;
import wang.yeting.wtp.admin.factory.WtpConfigFactory;
import wang.yeting.wtp.admin.model.vo.WtpVo;
import wang.yeting.wtp.admin.service.ServerService;
import wang.yeting.wtp.admin.service.WtpLogService;
import wang.yeting.wtp.admin.service.WtpRegistryService;
import wang.yeting.wtp.admin.service.WtpService;
import wang.yeting.wtp.admin.thread.WtpLogMonitorHelper;
import wang.yeting.wtp.core.biz.model.*;

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
    private final WtpLogMonitorHelper wtpLogMonitorHelper;


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
        wtpLogMonitorHelper.wtpLogMonitor(wtpLogBo);
        WtpLog wtpLog = new WtpLog();
        BeanUtils.copyProperties(wtpLogBo, wtpLog);
        return wtpLogService.create(wtpLog);
    }

    @Override
    public ConfigChangeEvent pullConfig(String appId, String clusterId, String ip) {
        log.info("pullConfig ------> appId：{}  --  clusterId：{}", appId, clusterId);
        wtpRegistryService.registry(new WtpRegistry().setAppId(appId).setClusterId(clusterId).setIp(ip));

        WtpConfigFactory wtpConfigFactory = WtpConfigFactory.getInstance();
        int count = 0;
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Boolean configChange = wtpConfigFactory.isConfigChange(appId, clusterId);
            if (configChange) {
                CopyOnWriteArrayList<Config> configList = wtpConfigFactory.getConfigChange(appId, clusterId, ip);
                ConcurrentMap<String, Config> configConcurrentMap = configList.stream().collect(Collectors.toConcurrentMap(Config::getName, Config -> Config));

                return new ConfigChangeEvent(configConcurrentMap);
            }
        } while (++count < 30);

        return new ConfigChangeEvent(new ConcurrentHashMap(1));
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
