package com.josue.batch.agent.core;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Created by Josue on 25/07/2016.
 */
public class CoreConfiguration {

    private final List<Class<? extends ChunkListener>> listeners = new LinkedList<>();
    private ThreadPoolExecutor service = defaultExecutor();
    private InstanceProvider provider = new SimpleInstanceProvider();
    private Level logLevel = Level.INFO;

    public CoreConfiguration executor(ThreadPoolExecutor executor) {
        this.service = executor;
        return this;
    }

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

    List<Class<? extends ChunkListener>> getListeners() {
        return this.listeners;
    }

    ThreadPoolExecutor getExecutor() {
        return service;
    }

    InstanceProvider getInstanceProvider() {
        return provider;
    }

    Level getLogLevel() {
        return logLevel;
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
