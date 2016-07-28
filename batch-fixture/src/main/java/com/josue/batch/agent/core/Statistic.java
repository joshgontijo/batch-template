package com.josue.batch.agent.core;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Josue on 26/07/2016.
 */
public class Statistic {

    private final int maximumPoolSize;
    private final int poolSize;
    private final long completedTaskCount;
    private final int activeCount;
    private final int corePoolSize;
    private final long taskCount;
    private final long remaining;

    public Statistic(ThreadPoolExecutor executor) {
        maximumPoolSize = executor.getMaximumPoolSize();
        poolSize = executor.getPoolSize();
        completedTaskCount = executor.getCompletedTaskCount();
        activeCount = executor.getActiveCount();
        corePoolSize = executor.getCorePoolSize();
        taskCount = executor.getTaskCount();
        remaining = executor.getQueue().remainingCapacity();
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

    public long getRemaining() {
        return remaining;
    }
}
