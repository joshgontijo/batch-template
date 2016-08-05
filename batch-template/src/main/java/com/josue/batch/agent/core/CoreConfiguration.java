package com.josue.batch.agent.core;

import com.josue.batch.agent.metric.Meter;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Josue on 25/07/2016.
 */
public class CoreConfiguration {

    private static final Logger logger = Logger.getLogger("BatchTemplate");

    private Meter meter = new Meter(true);
    private final List<Class<? extends ChunkListener>> listeners = new LinkedList<>();
    private final ThreadPoolExecutor executor = defaultExecutor();
    private InstanceProvider provider = new SimpleInstanceProvider();

    public CoreConfiguration() {
        logger.setLevel(Level.INFO);
    }

    public void instanceProvider(InstanceProvider provider) {
        this.provider = provider;
    }

    public void logLevel(Level level) {
        this.logger.setLevel(level);
    }

    public void executorCorePoolSize(int corePoolSize) {
        executor.setCorePoolSize(corePoolSize);
    }

    public void executorMaxPoolSize(int maxPoolSize) {
        executor.setMaximumPoolSize(maxPoolSize);
    }

    public void executorKeepAlive(int keepAliveMs) {
        executor.setKeepAliveTime(keepAliveMs, TimeUnit.MILLISECONDS);
    }

    public void executorThreadFactory(ThreadFactory factory) {
        executor.setThreadFactory(factory);
    }

    public List<Class<? extends ChunkListener>> getListeners() {
        return this.listeners;
    }

    InstanceProvider getInstanceProvider() {
        return provider;
    }

    Logger getLogger() {
        return logger;
    }

    ThreadPoolExecutor getExecutor() {
        return executor;
    }

    Meter getMeter() {
        return meter;
    }

    private ThreadPoolExecutor defaultExecutor() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                availableProcessors,
                availableProcessors * 2,
                2,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }

}
