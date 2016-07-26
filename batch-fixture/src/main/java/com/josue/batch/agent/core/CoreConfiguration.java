package com.josue.batch.agent.core;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

/**
 * Created by Josue on 25/07/2016.
 */
public class CoreConfiguration {

    private final List<Class<? extends ChunkListener>> listeners = new LinkedList<>();
    private ExecutorService service = Executors.newFixedThreadPool(5);
    private InstanceProvider provider = new SimpleInstanceProvider();
    private Level logLevel = Level.INFO;

    public CoreConfiguration executor(ExecutorService executorService) {
        this.service = executorService;
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

    ExecutorService getExecutorService() {
        return service;
    }

    InstanceProvider getInstanceProvider() {
        return provider;
    }

    Level getLogLevel(){
        return logLevel;
    }

}
