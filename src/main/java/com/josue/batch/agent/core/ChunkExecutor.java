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

    public ChunkExecutor(ChunkExecutorConfig<T> config, Properties properties) {
        try {
            reader = config.getReader().newInstance();
            reader.init(properties);

            processor = config.getProcessor().newInstance();
            reader.init(properties);

            writer = config.getWriter().newInstance();
            reader.init(properties);

            for (Class<? extends ChunkListener> listener : config.getListeners()) {
                ChunkListener chunkListener = listener.newInstance();
                chunkListener.init(properties);
                listeners.add(chunkListener);
            }

        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Could not initialise ChunkExecutor", e);
        }
    }

    public void execute() {
        //start
        for (ChunkListener listener : listeners) {
            listener.onStart();
        }

        List<T> processedItems = new LinkedList<>();
        try {
            T item;
            while ((item = reader.read()) != null) {
                item = processor.proccess(item);
                processedItems.add(item);
            }
            writer.write(processedItems);

        } catch (Exception ex) {
            for (ChunkListener listener : listeners) {
                listener.onFail(ex);
            }
            return;
        }

        //on sucess
        for (ChunkListener listener : listeners) {
            listener.onSuccess();
        }
    }

}
