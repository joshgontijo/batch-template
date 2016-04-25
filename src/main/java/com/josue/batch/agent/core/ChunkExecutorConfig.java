package com.josue.batch.agent.core;

import com.josue.batch.agent.chunk.ChunkProcessor;
import com.josue.batch.agent.chunk.ChunkReader;
import com.josue.batch.agent.chunk.ChunkWriter;

/**
 * Created by Josue on 25/04/2016.
 */
public class ChunkExecutorConfig<T> extends ExecutorConfig<T> {

    private Class<? extends ChunkReader<T>> reader;
    private Class<? extends ChunkProcessor<T>> processor;
    private Class<? extends ChunkWriter<T>> writer;

    public ChunkExecutorConfig(Class<? extends ChunkReader<T>> reader, Class<? extends ChunkWriter<T>> writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public ChunkExecutorConfig processor(Class<? extends ChunkProcessor<T>> processor) {
        this.processor = processor;
        return this;
    }

    protected Class<? extends ChunkReader<T>> getReader() {
        return reader;
    }

    protected Class<? extends ChunkProcessor<T>> getProcessor() {
        return processor;
    }

    protected Class<? extends ChunkWriter<T>> getWriter() {
        return writer;
    }
}
