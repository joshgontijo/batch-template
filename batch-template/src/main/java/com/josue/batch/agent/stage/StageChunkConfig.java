package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.CoreConfiguration;

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

    public void processor(Class<? extends StageChunkProcessor> processor) {
        this.processorType = processor;
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
