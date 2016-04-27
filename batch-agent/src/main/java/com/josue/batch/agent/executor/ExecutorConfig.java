package com.josue.batch.agent.executor;

import com.josue.batch.agent.core.ChunkListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Josue on 25/04/2016.
 */
public class ExecutorConfig {

    private List<Class<ChunkListener>> listeners = new LinkedList<>();

    public ExecutorConfig addListener(Class<ChunkListener> listener) {
        this.listeners.add(listener);
        return this;
    }

    public List<Class<ChunkListener>> getListeners() {
        return listeners;
    }
}
