package wang.yeting.wtp.admin.thread;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.admin.handler.DeferredResultHandler;

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
public class PullConfigMonitorHandler {

    private static ConcurrentMap<String, DeferredResultHandler> DeferredResultHandlerMap = new ConcurrentHashMap<>();

    public static void add(DeferredResultHandler DeferredResultHandler) {
        String key = DeferredResultHandler.getAppId() + DeferredResultHandler.getClusterId() + DeferredResultHandler.getIp();
        DeferredResultHandlerMap.put(key, DeferredResultHandler);
    }

    public static void remove(DeferredResultHandler DeferredResultHandler) {
        String key = DeferredResultHandler.getAppId() + DeferredResultHandler.getClusterId() + DeferredResultHandler.getIp();
        DeferredResultHandlerMap.remove(key);
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
        if (DeferredResultHandlerMap.size() == 0) {
            return;
        }
        Collection<DeferredResultHandler> DeferredResultHandlerCollection = DeferredResultHandlerMap.values();
        ArrayList<DeferredResultHandler> list = new ArrayList<>(DeferredResultHandlerCollection);
        int n = list.size() / 10 + (list.size() % 10 > 0 ? 1 : 0);
        if (n <= 0) {
            return;
        }
        List<List<DeferredResultHandler>> lists = averageAssign(list, n);
        for (List<DeferredResultHandler> DeferredResultHandlers : lists) {
            MainThreadPool.pullExecute(() -> {
                for (DeferredResultHandler DeferredResultHandler : DeferredResultHandlers) {
                    DeferredResultHandler.monitor();
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
