package wang.yeting.wtp.sample.test;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import wang.yeting.wtp.core.annotation.Wtp;
import wang.yeting.wtp.core.concurrent.WtpThreadPoolExecutor;
import wang.yeting.wtp.core.context.WtpAnnotationContext;
import wang.yeting.wtp.core.context.WtpAnnotationContextAware;
import wang.yeting.wtp.core.enums.QueueEnums;
import wang.yeting.wtp.core.enums.RejectedExecutionHandlerEnums;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author : weipeng
 * @date : 2020-07-23 14:15
 */

@Slf4j
@Component
public class Test implements WtpAnnotationContextAware {

    /**
     * 普通
     */
    @Wtp("test")
    private WtpThreadPoolExecutor threadPoolExecutor;

    /**
     * 方法赋值
     * 可在前后执行自己的方法
     */
    private WtpThreadPoolExecutor threadPoolExecutor2;

    @Wtp("test2")
    public void setWtpThreadPoolExecutor(WtpThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor2 = threadPoolExecutor;
    }

    /**
     * 自定义默认值
     * 云端配置 > 默认
     */
    @Wtp(value = "test3", defaultCorePoolSize = 10, defaultMaximumPoolSize = 50, defaultKeepAliveSeconds = 10L, defaultQueueCapacity = 200)
    private WtpThreadPoolExecutor threadPoolExecutor3;

    /**
     * 自定义默认队列  Rejected处理方式
     * 云端配置 > 默认
     */
    @Wtp(value = "test4", defaultQueueName = QueueEnums.resizableCapacityLinkedBlockIngQueue, rejectedExecutionHandlerName = RejectedExecutionHandlerEnums.callerRunsPolicy)
    private WtpThreadPoolExecutor threadPoolExecutor4;


    /**
     * 使用同一个
     */
    @Wtp("test5")
    private WtpThreadPoolExecutor threadPoolExecutor5;
    @Wtp("test5")
    private WtpThreadPoolExecutor threadPoolExecutor6;


    private ExecutorService threadPoolExecutor7;
    @Wtp("test7")
    public void setExecutorService(ExecutorService executor) {
        this.threadPoolExecutor7 = executor;
    }

    @Wtp("test7")
    private ExecutorService threadPoolExecutor8;


    public String getThreadPoolLog() {
        ArrayList<String> logList = new ArrayList<>();
        logList.add(threadPoolLog(threadPoolExecutor));
        logList.add(threadPoolLog(threadPoolExecutor2));
        logList.add(threadPoolLog(threadPoolExecutor3));
        logList.add(threadPoolLog(threadPoolExecutor4));
        logList.add(threadPoolLog(threadPoolExecutor5));
        logList.add(threadPoolLog(threadPoolExecutor6));
        String logs = "";
        for (String log : logList) {
            logs += log + "\n";
        }
        return logs;
    }

    private String threadPoolLog(WtpThreadPoolExecutor threadPool) {
        BlockingQueue<Runnable> queue = threadPool.getQueue();
        return "核心线程数：" + threadPool.getCorePoolSize()
                + " __活动线程数：" + threadPool.getActiveCount()
                + " __最大线程数：" + threadPool.getMaximumPoolSize()
                + " __任务完成数：" + threadPool.getCompletedTaskCount()
                + " __空闲线程销毁时间（秒）：" + threadPool.getKeepAliveTime(TimeUnit.SECONDS)
                + " __线程池活跃度：" + divide(threadPool.getActiveCount(), threadPool.getMaximumPoolSize())
                + " __队列大小：" + (queue.size() + queue.remainingCapacity())
                + " __当前排队线程数：" + queue.size()
                + " __队列剩余" + queue.remainingCapacity()
                + " __队列使用度" + divide(queue.size(), queue.size() + queue.remainingCapacity());

    }

    private String divide(int num1, int num2) {
        return String.format("%1.2f%%", Double.parseDouble(num1 + "") / Double.parseDouble(num2 + "") * 100);
    }


    public String testPool() {
        ArrayList<WtpThreadPoolExecutor> poolExecutorArrayList = new ArrayList<>();
        poolExecutorArrayList.add(threadPoolExecutor);
        poolExecutorArrayList.add(threadPoolExecutor2);
        poolExecutorArrayList.add(threadPoolExecutor3);
        poolExecutorArrayList.add(threadPoolExecutor4);
        poolExecutorArrayList.add(threadPoolExecutor5);
        poolExecutorArrayList.add(threadPoolExecutor6);
        int i = 0;
        while (true) {
            WtpThreadPoolExecutor threadPoolExecutor = poolExecutorArrayList.get(i++);
            if (i == poolExecutorArrayList.size()) {
                i = 0;
            }
            try {
                Thread.sleep(200);
                threadPoolExecutor.execute(() -> {
                    try {
                        int random = (int) (Math.random() * 10000);
                        log.info("等待" + random);
                        Thread.sleep(random);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set the {@link WtpAnnotationContext} that this component runs in.l
     * 获取所有注解信息
     *
     * @param wtpAnnotationContext
     */
    @Override
    public void setWtpAnnotationContext(WtpAnnotationContext wtpAnnotationContext) {
        log.info("wtpAnnotationContext = {}", JSONUtil.toJsonStr(wtpAnnotationContext.getWtpHandler()));
    }
}
