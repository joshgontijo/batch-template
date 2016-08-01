package com.josue.batch.agent.core;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by Josue on 25/07/2016.
 */
public class CoreConfiguration {

    private final List<Class<? extends ChunkListener>> listeners = new LinkedList<>();
    private ThreadPoolExecutor executor = defaultExecutor();
    private InstanceProvider provider = new SimpleInstanceProvider();
    private Level logLevel = Level.INFO;

    public CoreConfiguration instanceProvider(InstanceProvider provider) {
        this.provider = provider;
        return this;
    }

    public CoreConfiguration addListener(Class<? extends ChunkListener> listener) {
        this.listeners.add(listener);
        return this;
    }

    public CoreConfiguration logLevel(Level level) {
        this.logLevel = level;
        return this;
    }

    public CoreConfiguration executorCorePoolSize(int corePoolSize) {
        executor.setCorePoolSize(corePoolSize);
        return this;
    }


    public CoreConfiguration executorMaxPoolSize(int maxPoolSize) {
        executor.setMaximumPoolSize(maxPoolSize);
        return this;
    }

    public CoreConfiguration executorThreadFactory(ThreadFactory factory) {
        executor.setThreadFactory(factory);
        return this;
    }

    InstanceProvider getInstanceProvider() {
        return provider;
    }

    Level getLogLevel() {
        return logLevel;
    }

    ThreadPoolExecutor getExecutor() {
        return executor;
    }

    List<Class<? extends ChunkListener>> getListeners() {
        return this.listeners;
    }

    private ThreadPoolExecutor defaultExecutor() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                availableProcessors,
                availableProcessors * 2,
                2,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

}
