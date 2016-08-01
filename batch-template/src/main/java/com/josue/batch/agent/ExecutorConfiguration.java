package com.josue.batch.agent;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Josue on 26/07/2016.
 */
public class ExecutorConfiguration {

    private int corePoolSize;
    private int keepAliveTime;
    private int maxPoolSize;
    private int queueCapacity;
    private RejectedExecutionHandler rejectedExecutionHandler;

    private ExecutorConfiguration() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.corePoolSize = availableProcessors;
        this.maxPoolSize = availableProcessors * 2;
        this.keepAliveTime = 1;
        this.queueCapacity = 100;

    }

    public final static class Builder {

        ExecutorConfiguration configuration = new ExecutorConfiguration();

        public Builder corePoolSize(int corePoolSize) {
            configuration.corePoolSize = corePoolSize;
            return this;
        }

        public Builder keepAliveTime(int keepAliveTime) {
            configuration.keepAliveTime = keepAliveTime;
            return this;
        }

        public Builder maxPoolSize(int maxPoolSize) {
            configuration.maxPoolSize = maxPoolSize;
            return this;
        }

        public Builder queueCapacity(int queueCapacity) {
            configuration.queueCapacity = queueCapacity;
            return this;
        }

        public Builder abortPolicy() {
            configuration.rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();
            return this;
        }

        public Builder callerRunsPolicy() {
            configuration.rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
            return this;
        }

        public Builder discardPolicy() {
            configuration.rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();
            return this;
        }

        public Builder discardOldestPolicy() {
            configuration.rejectedExecutionHandler = new ThreadPoolExecutor.DiscardOldestPolicy();
            return this;
        }

        public Builder customRejectedExecutionHandler(RejectedExecutionHandler reh) {
            configuration.rejectedExecutionHandler = reh;
            return this;
        }

        void validateConfiguration() {
            if (this.configuration.keepAliveTime < 0) {
                throw new IllegalStateException("keepAliveTime is: " + this.configuration.keepAliveTime + " but should be > 0");
            }

            if (this.configuration.queueCapacity < 0) {
                throw new IllegalStateException("queueCapacity is: " + this.configuration.queueCapacity + " but should be > 0");
            }
            if (this.configuration.corePoolSize > this.configuration.maxPoolSize) {
                throw new IllegalStateException("corePoolSize(" + this.configuration.corePoolSize + ") is larger than maxPoolSize(" + this.configuration.maxPoolSize + ")");
            }
        }

        public ExecutorConfiguration build() {
            validateConfiguration();
            return configuration;

        }
    }


    public static final ExecutorConfiguration defaultConfiguration() {
        return new ExecutorConfiguration();
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return rejectedExecutionHandler;
    }

}