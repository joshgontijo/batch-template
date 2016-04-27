package com.josue.batch.agent.core;

import com.josue.batch.agent.stage.StageExecutorConfig;
import com.josue.batch.agent.stage.StageChunkProcessor;
import com.josue.batch.agent.stage.StageChunkReader;
import com.josue.batch.agent.stage.StageChunkWriter;
import com.josue.batch.agent.executor.Executor;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class ChunkExecutor<T> extends Executor {

    private final Class<StageChunkReader<T>> readerType;
    private final Class<StageChunkProcessor<T>> processorType;
    private final Class<StageChunkWriter<T>> writerType;

    private StageChunkReader<T> reader;
    private StageChunkProcessor<T> processor;
    private StageChunkWriter<T> writer;


    public ChunkExecutor(StageExecutorConfig<T> config) {
        super(config.getListeners());
        if (config.getReader() == null || config.getWriter() == null) {
            throw new IllegalArgumentException("Reader and writer must be specified");
        }
        this.readerType = config.getReader();
        this.processorType = config.getProcessor();
        this.writerType = config.getWriter();
    }


    @Override
    public void execute() throws Exception {
        List<T> processedItems = new LinkedList<>();
        T item;
        while ((item = reader.read()) != null) {
            if (processor != null) {
                item = processor.proccess(item);
            }
            processedItems.add(item);
        }
        writer.write(processedItems);
    }

    @Override
    public void init(Properties properties) throws Exception {
        reader = readerType.newInstance();
        reader.init(properties);

        if (processorType != null) {
            processor = processorType.newInstance();
            processor.init(properties);
        }

        writer = writerType.newInstance();
        writer.init(properties);
    }
}
