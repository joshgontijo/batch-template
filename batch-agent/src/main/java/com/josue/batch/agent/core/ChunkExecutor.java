package com.josue.batch.agent.core;

import com.josue.batch.agent.chunk.ChunkListener;
import com.josue.batch.agent.chunk.ChunkProcessor;
import com.josue.batch.agent.chunk.ChunkReader;
import com.josue.batch.agent.chunk.ChunkWriter;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class ChunkExecutor<T> {

    private final List<ChunkListener> listeners = new LinkedList<>();
    private final ChunkReader<T> reader;
    private final ChunkProcessor<T> processor;
    private final ChunkWriter<T> writer;

    public ChunkExecutor(ChunkExecutorConfig<T> config) {
        try {
            reader = config.getReader().newInstance();
            processor = config.getProcessor().newInstance();
            writer = config.getWriter().newInstance();

            for (Class<? extends ChunkListener> listener : config.getListeners()) {
                ChunkListener chunkListener = listener.newInstance();
                listeners.add(chunkListener);
            }

        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not initialise ChunkExecutor", e);
        }
    }

    public void execute(Properties properties) {
        List<T> processedItems = new LinkedList<>();
        try {
            //init properties
            reader.init(properties);
            processor.init(properties);
            writer.init(properties);
            for (ChunkListener listener : listeners) {
                listener.init(properties);
            }

            //onstart
            for (ChunkListener listener : listeners) {
                listener.onStart();
            }

            //read / process
            T item;
            while ((item = reader.read()) != null) {
                item = processor.proccess(item);
                processedItems.add(item);
            }

            //write
            writer.write(processedItems);

            //on sucess
            for (ChunkListener listener : listeners) {
                listener.onSuccess();
            }

        } catch (Exception ex) {
            //on error
            for (ChunkListener listener : listeners) {
                listener.onFail(ex);
            }
            return;
        }
    }

}
