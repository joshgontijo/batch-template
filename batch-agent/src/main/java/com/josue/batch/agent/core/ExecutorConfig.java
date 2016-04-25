package com.josue.batch.agent.core;

import com.josue.batch.agent.chunk.ChunkListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Josue on 25/04/2016.
 */
public class ExecutorConfig<T> {

    private List<Class<? extends ChunkListener>> listeners = new LinkedList<>();

    public ExecutorConfig addListener(Class<? extends ChunkListener> listener) {
        this.listeners.add(listener);
        return this;
    }

    protected List<Class<? extends ChunkListener>> getListeners() {
        return listeners;
    }
}
