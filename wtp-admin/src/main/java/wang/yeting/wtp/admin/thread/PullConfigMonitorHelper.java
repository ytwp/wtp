package wang.yeting.wtp.admin.thread;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.admin.handler.DeferredResultHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author : weipeng
 * @date : 2020-07-30 19:56
 */

@Slf4j
public class PullConfigMonitorHelper {

    private static ConcurrentMap<String, DeferredResultHelper> deferredResultHelperMap = new ConcurrentHashMap<>();

    public static void add(DeferredResultHelper deferredResultHelper) {
        String key = deferredResultHelper.getAppId() + deferredResultHelper.getClusterId() + deferredResultHelper.getIp();
        deferredResultHelperMap.put(key, deferredResultHelper);
    }

    public static void remove(DeferredResultHelper deferredResultHelper) {
        String key = deferredResultHelper.getAppId() + deferredResultHelper.getClusterId() + deferredResultHelper.getIp();
        deferredResultHelperMap.remove(key);
    }

    public void pullConfigMonitor() {
        while (MainThreadPool.monitorRun) {
            try {
                doPullConfigMonitor();
                Thread.sleep(1000L);
            } catch (Exception e) {
                log.error("wtp ------> doPullConfigMonitor Exception = [{}]. ", e);
            }
        }
    }

    /**
     * Traversing all requests per second, monitoring configuration changes.
     * One thread handles 10 requests, guaranteeing a quick refresh.
     */
    private void doPullConfigMonitor() {
        log.info("wtp ------> doPullConfigMonitor . ");
        if (deferredResultHelperMap.size() == 0) {
            return;
        }
        Collection<DeferredResultHelper> deferredResultHelperCollection = deferredResultHelperMap.values();
        ArrayList<DeferredResultHelper> list = new ArrayList<>(deferredResultHelperCollection);
        int n = list.size() / 10 + (list.size() % 10 > 0 ? 1 : 0);
        if (n <= 0) {
            return;
        }
        List<List<DeferredResultHelper>> lists = averageAssign(list, n);
        for (List<DeferredResultHelper> deferredResultHelpers : lists) {
            MainThreadPool.pullExecute(() -> {
                for (DeferredResultHelper deferredResultHelper : deferredResultHelpers) {
                    deferredResultHelper.monitor();
                }
            });
        }
    }

    private static <T> List<List<T>> averageAssign(List<T> list, int n) {
        List<List<T>> result = new ArrayList<>();
        int remaider = list.size() % n;
        int number = list.size() / n;
        int offset = 0;
        for (int i = 0; i < n; i++) {
            List<T> value;
            if (remaider > 0) {
                value = list.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = list.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }
}
