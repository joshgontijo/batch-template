package com.josue.batch.agent.core;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Josue on 25/04/2016.
 */
public class BatchConfig {

    private static final int DEFAULT_THREADS = 2;
    private static final int DEFAULT_QUEUE_SIZE = 100000;

    private ThreadPoolExecutor executor;
    private BlockingQueue<Properties> chunkQueue;

    public BatchConfig() {
        this(DEFAULT_THREADS, DEFAULT_QUEUE_SIZE);
    }

    public BatchConfig(int threads, int queueSize) {
        chunkQueue = new ArrayBlockingQueue<>(queueSize);
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
    }

    public BatchConfig executor(ExecutorService executorService) {
        this.executor = (ThreadPoolExecutor) executorService;
        return this;
    }

    public BatchConfig queueSize(int size) {
        chunkQueue = new ArrayBlockingQueue<>(size);
        return this;
    }

    protected ThreadPoolExecutor getExecutor() {
        return this.executor;
    }

    protected BlockingQueue getQueue() {
        return this.chunkQueue;
    }

}
