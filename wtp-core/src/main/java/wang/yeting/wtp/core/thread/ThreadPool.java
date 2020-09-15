package wang.yeting.wtp.core.thread;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : weipeng
 * @date : 2020-07-24 17:19
 */

public class ThreadPool {

    private static ThreadPoolExecutor threadPoolExecutor = null;

    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = null;

    public static void loadMainThreadPool(ThreadPoolExecutor threadPool, ScheduledThreadPoolExecutor scheduledThreadPool) {
        threadPoolExecutor = threadPool;
        scheduledThreadPoolExecutor = scheduledThreadPool;
    }

    public static void execute(Runnable command) {
        threadPoolExecutor.execute(command);
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public static ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
        return scheduledThreadPoolExecutor;
    }

    private ThreadPool() {
    }

}
