package wang.yeting.wtp.core.factory;

import lombok.extern.slf4j.Slf4j;
import wang.yeting.wtp.core.annotation.Wtp;
import wang.yeting.wtp.core.biz.model.Config;
import wang.yeting.wtp.core.biz.model.ConfigEvent;
import wang.yeting.wtp.core.concurrent.ResizableCapacityLinkedBlockingDeque;
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


    private final ConcurrentMap<String, BlockingQueue<Runnable>> queueConcurrentMap = new ConcurrentHashMap<>();

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
     * @param configEvent {@link ConfigEvent}
     */
    public void initQueue(ConfigEvent configEvent) {
        Set<String> nameSet = configEvent.changedKeys();
        for (String name : nameSet) {
            Config change = configEvent.getChange(name);
            String queueName = change.getQueueName();
            Integer queueCapacity = change.getQueueCapacity();
            BlockingQueue<Runnable> blockingQueue = createQueue(queueName, queueCapacity);
            queueConcurrentMap.put(name, blockingQueue);
        }
    }

    private BlockingQueue<Runnable> createQueue(String queueName, Integer queueCapacity) {
        if (Objects.equals(QueueEnums.resizableCapacityLinkedBlockIngQueue.getQueueName(), queueName)) {
            return new ResizableCapacityLinkedBlockingQueue<>(queueCapacity);
        } else if (Objects.equals(QueueEnums.resizableCapacityLinkedBlockingDeque.getQueueName(), queueName)) {
            return new ResizableCapacityLinkedBlockingDeque<>(queueCapacity);
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
                ResizableCapacityLinkedBlockingQueue<Runnable> queue = (ResizableCapacityLinkedBlockingQueue<Runnable>) blockingQueue;
                queue.setCapacity(queueCapacity);
            } else if (blockingQueue instanceof ResizableCapacityLinkedBlockingDeque) {
                ResizableCapacityLinkedBlockingDeque<Runnable> queue = (ResizableCapacityLinkedBlockingDeque<Runnable>) blockingQueue;
                queue.setCapacity(queueCapacity);
            } else if (blockingQueue instanceof LinkedBlockingQueue) {
                log.warn("wtp ------>  Dynamic resizing of LinkedBlockingQueue is not supported .");
            } else if (blockingQueue instanceof ArrayBlockingQueue) {
                log.warn("wtp ------>  Dynamic resizing of ArrayBlockingQueue is not supported .");
            } else if (blockingQueue instanceof LinkedBlockingDeque) {
                log.warn("wtp ------>  Dynamic resizing of LinkedBlockingDeque is not supported .");
            } else if (blockingQueue instanceof PriorityBlockingQueue) {
                log.warn("wtp ------>  Dynamic resizing of PriorityBlockingQueue is not supported .");
            } else if (blockingQueue instanceof SynchronousQueue) {
                log.warn("wtp ------>  Dynamic resizing of SynchronousQueue is not supported .");
            } else if (blockingQueue instanceof LinkedTransferQueue) {
                log.warn("wtp ------>  Dynamic resizing of LinkedTransferQueue is not supported .");
            } else {
                log.warn("wtp ------>  Incorrect Queue configuration .");
            }
        }
    }

}
