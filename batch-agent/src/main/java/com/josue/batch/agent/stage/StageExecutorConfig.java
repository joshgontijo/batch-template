package com.josue.batch.agent.stage;

/**
 * Created by Josue on 25/04/2016.
 */
public class StageExecutorConfig<T> extends com.josue.batch.agent.executor.ExecutorConfig {

    private Class<StageChunkReader<T>> reader;
    private Class<StageChunkProcessor<T>> processor;
    private Class<StageChunkWriter<T>> writer;

    public StageExecutorConfig(Class<StageChunkReader<T>> reader, Class<StageChunkWriter<T>> writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public StageExecutorConfig processor(Class<StageChunkProcessor<T>> processor) {
        this.processor = processor;
        return this;
    }

    public Class<StageChunkReader<T>> getReader() {
        return reader;
    }

    public Class<StageChunkProcessor<T>> getProcessor() {
        return processor;
    }

    public Class<StageChunkWriter<T>> getWriter() {
        return writer;
    }
}
