package wang.yeting.wtp.core.factory;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.core.annotation.Wtp;
import wang.yeting.wtp.core.biz.model.Config;
import wang.yeting.wtp.core.biz.model.ConfigEvent;
import wang.yeting.wtp.core.concurrent.ResizableCapacityLinkedBlockingQueue;
import wang.yeting.wtp.core.enums.QueueEnums;
import wang.yeting.wtp.core.exceptions.WtpConfigException;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author : weipeng
 * @date : 2020-07-23 23:03
 */
@Slf4j
public class WtpQueueFactory {


    private ConcurrentMap<String, BlockingQueue<Runnable>> queueConcurrentMap = new ConcurrentHashMap<>();

    private static WtpQueueFactory wtpQueueFactory = new WtpQueueFactory();

    public static WtpQueueFactory getInstance() {
        return wtpQueueFactory;
    }

    public static void refreshInstance() {
        wtpQueueFactory = new WtpQueueFactory();
    }

    public BlockingQueue<Runnable> getQueue(String name) {
        return queueConcurrentMap.get(name);
    }

    public ConcurrentMap<String, BlockingQueue<Runnable>> getQueueConcurrentMap() {
        return queueConcurrentMap;
    }

    public BlockingQueue<Runnable> loadDefaultQueue(Wtp wtp) {
        String name = wtp.value();
        String queueName = wtp.defaultQueueName().getQueueName();
        Integer queueCapacity = wtp.defaultQueueCapacity();
        BlockingQueue<Runnable> blockingQueue = createQueue(queueName, queueCapacity);
        queueConcurrentMap.put(name, blockingQueue);
        return blockingQueue;
    }

    /**
     * init Queue
     *
     * @param configEvent
     */
    public void initQueue(ConfigEvent configEvent) {
        Set<String> nameSet = configEvent.changedKeys();
        for (String name : nameSet) {
            Config change = configEvent.getChange(name);
            String queueName = change.getQueueName();
            Integer queueCapacity = change.getQueueCapacity();
            BlockingQueue blockingQueue = createQueue(queueName, queueCapacity);
            queueConcurrentMap.put(name, blockingQueue);
        }
    }

    private BlockingQueue<Runnable> createQueue(String queueName, Integer queueCapacity) {
        if (Objects.equals(QueueEnums.resizableCapacityLinkedBlockIngQueue.getQueueName(), queueName)) {
            return new ResizableCapacityLinkedBlockingQueue<>(queueCapacity);
        } else if (Objects.equals(QueueEnums.linkedBlockingQueue.getQueueName(), queueName)) {
            return new LinkedBlockingQueue<>(queueCapacity);
        } else if (Objects.equals(QueueEnums.arrayBlockingQueue.getQueueName(), queueName)) {
            return new ArrayBlockingQueue<>(queueCapacity);
        } else if (Objects.equals(QueueEnums.linkedBlockingDeque.getQueueName(), queueName)) {
            return new LinkedBlockingDeque<>(queueCapacity);
        } else if (Objects.equals(QueueEnums.priorityBlockingQueue.getQueueName(), queueName)) {
            return new PriorityBlockingQueue<>(queueCapacity);
        } else if (Objects.equals(QueueEnums.synchronousQueue.getQueueName(), queueName)) {
            return new SynchronousQueue<>();
        } else if (Objects.equals(QueueEnums.linkedTransferQueue.getQueueName(), queueName)) {
            return new LinkedTransferQueue<>();
        } else {
            throw new WtpConfigException("wtp ------> Queue configuration error. ");
        }
    }

    public void changeQueue(Config config) {
        String name = config.getName();
        Integer queueCapacity = config.getQueueCapacity();
        BlockingQueue<Runnable> blockingQueue = queueConcurrentMap.get(name);
        if ((blockingQueue.size() + blockingQueue.remainingCapacity()) != queueCapacity) {
            if (blockingQueue instanceof ResizableCapacityLinkedBlockingQueue) {
                ResizableCapacityLinkedBlockingQueue queue = (ResizableCapacityLinkedBlockingQueue) blockingQueue;
                queue.setCapacity(queueCapacity);
            } else if (blockingQueue instanceof LinkedBlockingQueue) {
                log.error("wtp ------>  Dynamic resizing of LinkedBlockingQueue is not supported .");
            } else if (blockingQueue instanceof ArrayBlockingQueue) {
                log.error("wtp ------>  Dynamic resizing of LinkedBlockingQueue is not supported .");
            } else if (blockingQueue instanceof LinkedBlockingDeque) {
                log.error("wtp ------>  Dynamic resizing of LinkedBlockingQueue is not supported .");
            } else if (blockingQueue instanceof PriorityBlockingQueue) {
                log.error("wtp ------>  Dynamic resizing of LinkedBlockingQueue is not supported .");
            } else {
                log.error("wtp ------>  Incorrect Queue configuration .");
            }
        }
    }

}
