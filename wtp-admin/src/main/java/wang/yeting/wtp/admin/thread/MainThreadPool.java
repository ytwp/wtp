package wang.yeting.wtp.admin.thread;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author : weipeng
 * @date : 2020-07-24 17:19
 */

public class MainThreadPool {

    private static ThreadPoolExecutor threadPool = null;

    public static void loadMainThreadPool(ThreadPoolExecutor threadPoolExecutor) {
        threadPool = threadPoolExecutor;
    }

    public static void execute(Runnable command) throws Exception {
        threadPool.execute(command);
    }


}
