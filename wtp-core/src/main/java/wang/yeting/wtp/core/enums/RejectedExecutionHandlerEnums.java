package wang.yeting.wtp.core.enums;

import lombok.Getter;
import wang.yeting.wtp.core.concurrent.WtpRejectedExecutionHandler;
import wang.yeting.wtp.core.concurrent.WtpThreadPoolExecutor;
import wang.yeting.wtp.core.exceptions.WtpConfigException;

/**
 * @author : weipeng
 * @date : 2020-08-10 14:21
 */
@Getter
public enum RejectedExecutionHandlerEnums {

    /**
     * A handler for rejected tasks that throws a
     */
    abortPolicy("AbortPolicy", new WtpThreadPoolExecutor.AbortPolicy()),

    /**
     * A handler for rejected tasks that runs the rejected task
     * directly in the calling thread of the {@code execute} method,
     * unless the executor has been shut down, in which case the task
     * is discarded.
     */
    callerRunsPolicy("CallerRunsPolicy", new WtpThreadPoolExecutor.CallerRunsPolicy()),

    /**
     * A handler for rejected tasks that silently discards the
     * rejected task.
     */
    discardPolicy("DiscardPolicy", new WtpThreadPoolExecutor.DiscardPolicy()),

    /**
     * A handler for rejected tasks that discards the oldest unhandled
     * request and then retries {@code execute}, unless the executor
     * is shut down, in which case the task is discarded.
     */
    discardOldestPolicy("DiscardOldestPolicy", new WtpThreadPoolExecutor.DiscardOldestPolicy());

    private String rejectedExecutionHandlerName;
    private WtpRejectedExecutionHandler wtpRejectedExecutionHandler;


    RejectedExecutionHandlerEnums(String rejectedExecutionHandlerName, WtpRejectedExecutionHandler wtpRejectedExecutionHandler) {
        this.rejectedExecutionHandlerName = rejectedExecutionHandlerName;
        this.wtpRejectedExecutionHandler = wtpRejectedExecutionHandler;
    }

    private static final Map<String, RejectedExecutionHandlerEnums> ENUM_MAP = new HashMap<>(RejectedExecutionHandlerEnums.values().length);

    static {
        for (RejectedExecutionHandlerEnums e : RejectedExecutionHandlerEnums.values()) {
            ENUM_MAP.put(e.getRejectedExecutionHandlerName(), e);
        }
    }

    /**
     * 根据 {@queueName rejectedExecutionHandlerName} 获取枚举类
     *
     * @param rejectedExecutionHandlerName 枚举类的值
     * @return 枚举类
     */
    public static RejectedExecutionHandlerEnums getByRejectedExecutionHandlerName(String rejectedExecutionHandlerName) {
        return ENUM_MAP.get(rejectedExecutionHandlerName);
    }

    public static List<String> getAllRejectedExecutionHandlerName() {
        Set<String> keySet = ENUM_MAP.keySet();
        return new ArrayList<>(keySet);
    }

    public static WtpRejectedExecutionHandler getRejectedExecutionHandler(String rejectedExecutionHandlerName) {
        RejectedExecutionHandlerEnums rejectedExecutionHandlerEnums = ENUM_MAP.get(rejectedExecutionHandlerName);
        if (rejectedExecutionHandlerEnums == null) {
            throw new WtpConfigException("wtp ------> [" + rejectedExecutionHandlerName + "] RejectedExecutionHandlerName configuration errors. ");
        }
        return rejectedExecutionHandlerEnums.wtpRejectedExecutionHandler;
    }
}
