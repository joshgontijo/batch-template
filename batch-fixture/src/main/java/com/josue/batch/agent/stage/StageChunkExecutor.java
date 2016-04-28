package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.ChunkListener;
import com.josue.batch.agent.core.Executor;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * Created by Josue on 19/04/2016.
 */
public class StageChunkExecutor<T> extends Executor {

    private final Class<? extends StageChunkReader<T>> readerType;
    private final Class<? extends StageChunkProcessor<T>> processorType;
    private final Class<? extends StageChunkWriter<T>> writerType;

    public StageChunkExecutor(Class<? extends StageChunkReader<T>> readerType,
                              Class<? extends StageChunkProcessor<T>> processorType,
                              Class<? extends StageChunkWriter<T>> writerType,
                              List<Class<? extends ChunkListener>> listeners,
                              ExecutorService service) {
        super(service, listeners);
        if (readerType == null || writerType == null) {
            throw new IllegalArgumentException("Reader and writer must be specified");
        }
        this.readerType = readerType;
        this.processorType = processorType;
        this.writerType = writerType;
    }


    @Override
    public void execute(Properties properties) throws Exception {
        StageChunkReader<T> reader = readerType.newInstance();
        reader.init(properties);

        StageChunkProcessor<T> processor = null;
        if (processorType != null) {
            processor = processorType.newInstance();
            processor.init(properties);
        }

        StageChunkWriter<T> writer = writerType.newInstance();
        writer.init(properties);


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

}
