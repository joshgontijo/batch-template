package com.josue.batch.agent.metric;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Josue on 26/07/2016.
 */
public class Metric {

    private final Executor executor;
    private final Meter meter;

    public Metric(ThreadPoolExecutor executor, Meter meter) {
        this.executor = new Executor(executor);
        this.meter = meter;
    }

    public Executor getExecutor() {
        return executor;
    }

    class Executor {
        private final int maximumPoolSize;
        private final int poolSize;
        private final long completedTaskCount;
        private final int activeCount;
        private final int corePoolSize;
        private final long taskCount;
        private final long queueSize;
        private final int queueRemainingSize;
        private final long keepAlive;

        public Executor(ThreadPoolExecutor executor) {
            maximumPoolSize = executor.getMaximumPoolSize();
            poolSize = executor.getPoolSize();
            completedTaskCount = executor.getCompletedTaskCount();
            activeCount = executor.getActiveCount();
            corePoolSize = executor.getCorePoolSize();
            taskCount = executor.getTaskCount();
            queueSize = executor.getQueue().size();
            keepAlive = executor.getKeepAliveTime(TimeUnit.MILLISECONDS);
            queueRemainingSize = executor.getQueue().remainingCapacity();
        }

        public int getMaximumPoolSize() {
            return maximumPoolSize;
        }

        public int getPoolSize() {
            return poolSize;
        }

        public long getCompletedTaskCount() {
            return completedTaskCount;
        }

        public int getActiveCount() {
            return activeCount;
        }

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public long getTaskCount() {
            return taskCount;
        }

        public long getQueueSize() {
            return queueSize;
        }

        public int getQueueRemainingSize() {
            return queueRemainingSize;
        }

        public long getKeepAlive() {
            return keepAlive;
        }
    }
}
