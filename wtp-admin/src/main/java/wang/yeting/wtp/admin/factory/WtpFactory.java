package wang.yeting.wtp.admin.factory;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.admin.bean.Wtp;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author : weipeng
 * @date : 2020-07-23 23:03
 */
@Slf4j
public class WtpFactory {

    private ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, Wtp>>> wtpConcurrentMap = new ConcurrentHashMap<>();

    private static WtpFactory wtpFactory = new WtpFactory();

    public static WtpFactory getInstance() {
        return wtpFactory;
    }

    public static void refreshInstance() {
        wtpFactory = new WtpFactory();
    }

    public ConcurrentMap<String, ConcurrentMap<String, Wtp>> getWtpConcurrentMap(String appId) {
        return wtpConcurrentMap.get(appId);
    }

    public ConcurrentMap<String, Wtp> getWtpConcurrentMap(String appId, String clusterId) {
        return wtpConcurrentMap.get(appId).get(clusterId);
    }

    public Wtp getWtp(String appId, String clusterId, String name) {
        return wtpConcurrentMap.get(appId).get(clusterId).get(name);
    }

    public void loadWtp(List<Wtp> wtpList) {
        log.info("wtp ------> wtpList:[{}]", wtpList);
        for (Wtp wtp : wtpList) {
            processWtp(wtp);
        }
    }

    public Boolean change(Wtp wtp) {
        try {
            processWtp(wtp);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void processWtp(Wtp wtp) {
        String appId = wtp.getAppId();
        String clusterId = wtp.getClusterId();
        ConcurrentMap<String, ConcurrentMap<String, Wtp>> concurrentMap = wtpConcurrentMap.computeIfAbsent(appId, k -> new ConcurrentHashMap<>());
        ConcurrentMap<String, Wtp> wtpMap = concurrentMap.computeIfAbsent(clusterId, k -> new ConcurrentHashMap<>());
        wtpMap.put(wtp.getName(), wtp);
    }
}
