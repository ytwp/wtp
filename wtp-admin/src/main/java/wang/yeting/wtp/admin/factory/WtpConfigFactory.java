package wang.yeting.wtp.admin.factory;

import cn.hutool.core.collection.CollectionUtil;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import wang.yeting.wtp.admin.bean.Wtp;
import wang.yeting.wtp.admin.bean.WtpRegistry;
import wang.yeting.wtp.admin.util.RedisUtils;
import wang.yeting.wtp.core.biz.model.Config;

import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author : weipeng
 * @date : 2020-07-23 23:03
 */
@Slf4j
public class WtpConfigFactory {

    private RedisUtils redisUtils;

    private static String CONFIG_ALL = "config:all";
    private static String CONFIG_ALL_CLUSTER_ID = "config:all:appId:%s:clusterId:%s";

    private static String CONFIG_CHANGE = "config:change";
    private static String CONFIG_CHANGE_CLUSTER_ID = "config:change:appId:%s:clusterId:%s";

    private static final Type CopyOnWriteArrayListType = TypeToken.getParameterized(CopyOnWriteArrayList.class, Config.class).getType();
    private static final Type ListStringType = TypeToken.getParameterized(List.class, String.class).getType();

    private static WtpConfigFactory wtpConfigFactory;

    public static WtpConfigFactory getInstance() {
        return wtpConfigFactory;
    }

    public static void refreshInstance(RedisUtils redisUtils) {
        wtpConfigFactory = new WtpConfigFactory(redisUtils);
    }

    private WtpConfigFactory(RedisUtils redisUtils) {
        this.redisUtils = redisUtils;
    }

    public ConcurrentMap<String, CopyOnWriteArrayList<Config>> getConfigConcurrentMap(String appId) {
        ConcurrentMap<String, CopyOnWriteArrayList<Config>> wtpAppConfigChangeConcurrentMap = new ConcurrentHashMap<>();
        List<String> clusterIdList = redisUtils.getMapToType(CONFIG_ALL, appId, ListStringType);
        for (String clusterId : clusterIdList) {
            CopyOnWriteArrayList<Config> wtpConfigChangeList = redisUtils.get(String.format(CONFIG_ALL_CLUSTER_ID, appId, clusterId), CopyOnWriteArrayListType);
            if (CollectionUtil.isNotEmpty(wtpConfigChangeList)) {
                wtpAppConfigChangeConcurrentMap.put(clusterId, wtpConfigChangeList);
            }
        }
        return wtpAppConfigChangeConcurrentMap;
    }

    public CopyOnWriteArrayList<Config> getConfigList(String appId, String clusterId) {
        CopyOnWriteArrayList<Config> wtpConfigChangeList = redisUtils.get(String.format(CONFIG_ALL_CLUSTER_ID, appId, clusterId), CopyOnWriteArrayListType);
        return wtpConfigChangeList == null ? new CopyOnWriteArrayList<>() : wtpConfigChangeList;
    }

    public ConcurrentMap<String, ConcurrentMap<String, CopyOnWriteArrayList<Config>>> getWtpConfigConcurrentMap() {
        ConcurrentMap<String, ConcurrentMap<String, CopyOnWriteArrayList<Config>>> wtpConfigChangeConcurrentMap = new ConcurrentHashMap<>();
        Set<String> appIdSet = redisUtils.hashStrKeys(CONFIG_ALL);
        for (String appId : appIdSet) {
            ConcurrentMap<String, CopyOnWriteArrayList<Config>> configConcurrentMap = getConfigConcurrentMap(appId);
            wtpConfigChangeConcurrentMap.put(appId, configConcurrentMap);
        }
        return wtpConfigChangeConcurrentMap;
    }

    /**
     * load configuration.
     *
     * @param wtpList
     */
    public void loadConfig(List<Wtp> wtpList) {
        log.info("wtp ------>loadConfig wtpList:[{}]", wtpList);
        Map<String, List> appMap = new HashMap();
        Map<String, List> configMap = new HashMap();
        for (Wtp wtp : wtpList) {
            String appId = wtp.getAppId();
            String clusterId = wtp.getClusterId();
            List<String> clusterIdList = appMap.computeIfAbsent(appId, k -> new ArrayList<>());
            clusterIdList.add(clusterId);

            Config config = new Config();
            BeanUtils.copyProperties(wtp, config);
            List configList = configMap.computeIfAbsent(appId + "-_-" + clusterId, k -> new ArrayList<>());
            configList.add(config);
        }
        for (Map.Entry<String, List> entry : configMap.entrySet()) {
            String appIdAndClusterId = entry.getKey();
            String[] split = appIdAndClusterId.split("-_-");
            List list = entry.getValue();
            redisUtils.set(String.format(CONFIG_ALL_CLUSTER_ID, split[0], split[1]), list);
        }
        redisUtils.hashPutAllList(CONFIG_ALL, appMap);
    }

    /**
     * Determines that the configuration has changed.
     *
     * @param appId
     * @param clusterId
     * @return true if the config is changed, false otherwise.
     */
    public Boolean isConfigChange(String appId, String clusterId) {
        Long hashSize = redisUtils.hashSize(String.format(CONFIG_CHANGE_CLUSTER_ID, appId, clusterId));
        return hashSize > 0;
    }

    /**
     * Gets the configuration changed, It will be deleted.
     *
     * @param appId
     * @param clusterId
     * @return A collection of changed configurations.
     */
    public CopyOnWriteArrayList<Config> getConfigChange(String appId, String clusterId, String ip) {
        String key = String.format(CONFIG_CHANGE_CLUSTER_ID, appId, clusterId);
        CopyOnWriteArrayList<Config> configChangeList = redisUtils.getMapToType(key, ip, CopyOnWriteArrayListType);
        // Deletes the value already obtained
        redisUtils.delete(key, ip);
        return configChangeList == null ? new CopyOnWriteArrayList<>() : configChangeList;
    }

    public Boolean change(Wtp wtp, List<WtpRegistry> wtpRegistryList) {
        try {
            Config config = new Config();
            BeanUtils.copyProperties(wtp, config);
            for (WtpRegistry wtpRegistry : wtpRegistryList) {
                CopyOnWriteArrayList<Config> changeConfigList = getChangeConfigList(wtpRegistry.getIp(), wtp.getClusterId(), wtpRegistry.getIp());
                changeConfigList.add(config);
                String key = String.format(CONFIG_CHANGE_CLUSTER_ID, wtp.getAppId(), wtp.getClusterId());
                redisUtils.hashPut(key, wtpRegistry.getIp(), changeConfigList);
                redisUtils.expire(key, 600);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Gets the configuration changed.
     *
     * @param appId
     * @param clusterId
     * @param ip
     * @return
     */
    public CopyOnWriteArrayList<Config> getChangeConfigList(String appId, String clusterId, String ip) {
        CopyOnWriteArrayList<Config> configChangeList = redisUtils.getMapToType(String.format(CONFIG_CHANGE_CLUSTER_ID, appId, clusterId), ip, CopyOnWriteArrayListType);
        return configChangeList == null ? new CopyOnWriteArrayList<>() : configChangeList;
    }

}
