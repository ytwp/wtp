package wang.yeting.wtp.admin.thread;

import java.util.concurrent.*;

/**
 * @author : weipeng
 * @date : 2020-07-24 17:19
 */

public class MainThreadPool {

    private static ThreadPoolExecutor threadPool = null;

    private static ExecutorService executorService = null;

    public volatile static boolean monitorRun = true;

    public static void loadMainThreadPool(ThreadPoolExecutor threadPoolExecutor, ExecutorService pullConfigExecutor) {
        threadPool = threadPoolExecutor;
        executorService = pullConfigExecutor;
    }

    public static void execute(Runnable command) {
        threadPool.execute(command);
    }

    public static void pullExecute(Runnable command) {
        executorService.execute(command);
    }

    public static void destroy() {
        monitorRun = false;
        executorService.shutdown();
        threadPool.shutdown();
    }
}
