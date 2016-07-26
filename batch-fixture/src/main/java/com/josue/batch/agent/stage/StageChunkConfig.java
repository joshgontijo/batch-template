package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.ChunkListener;
import com.josue.batch.agent.core.CoreConfiguration;
import com.josue.batch.agent.core.InstanceProvider;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;

/**
 * Created by Josue on 25/07/2016.
 */
public class StageChunkConfig extends CoreConfiguration {

    private final Class<? extends StageChunkReader> readerType;
    private final Class<? extends StageChunkWriter> writerType;
    private Class<? extends StageChunkProcessor> processorType;

    public StageChunkConfig(Class<? extends StageChunkReader> reader,
                            Class<? extends StageChunkWriter> writer) {
        this.readerType = reader;
        this.writerType = writer;
    }

    public StageChunkConfig(Class<? extends StageChunkReader> reader,
                            Class<? extends StageChunkProcessor> processor,
                            Class<? extends StageChunkWriter> writer) {
        this.readerType = reader;
        this.processorType = processor;
        this.writerType = writer;
    }

    public StageChunkConfig processor(Class<? extends StageChunkProcessor> processor) {
        this.processorType = processor;
        return this;
    }

    @Override
    public StageChunkConfig executor(ThreadPoolExecutor executor) {
        super.executor(executor);
        return this;
    }

    @Override
    public StageChunkConfig instanceProvider(InstanceProvider provider) {
        super.instanceProvider(provider);
        return this;
    }

    @Override
    public StageChunkConfig addListener(Class<? extends ChunkListener> listener) {
        super.addListener(listener);
        return this;
    }

    @Override
    public StageChunkConfig logLevel(Level level) {
        super.logLevel(level);
        return this;
    }

    Class<? extends StageChunkReader> getReader() {
        return readerType;
    }

    Class<? extends StageChunkProcessor> getProcessor() {
        return processorType;
    }

    Class<? extends StageChunkWriter> getWriter() {
        return writerType;
    }


}
