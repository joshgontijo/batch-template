package com.josue.batch.agent.chunk;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class ChunkExecutor<T> {

    private final List<Class<? extends ChunkListener>> listeners = new LinkedList<>();
    private final Class<? extends ChunkReader<T>> reader;
    private final Class<? extends ChunkProcessor<T>> processor;
    private final Class<? extends ChunkWriter<T>> writer;

    public ChunkExecutor(Class<? extends ChunkReader<T>> reader, Class<? extends ChunkProcessor<T>> processor, Class<? extends ChunkWriter<T>> writer) {
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
    }

    public void execute() {
        execute(new Properties());
    }

    public void execute(Properties props) {
        ChunkReader<T> readerClone = null;
        ChunkProcessor<T> processorClone = null;
        ChunkWriter<T> writerClone = null;
        final List<ChunkListener> clonedListeners = new LinkedList<>();
        try {
            readerClone = reader.newInstance();
            readerClone.init(props);

            processorClone = processor.newInstance();
            processorClone.init(props);

            writerClone = writer.newInstance();
            writerClone.init(props);

            for (Class<? extends ChunkListener> listener : listeners) {
                ChunkListener clonedList = listener.newInstance();
                clonedList.init(props);
                clonedListeners.add(clonedList);
            }

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //start
        for (ChunkListener listener : clonedListeners) {
            listener.onStart();
        }

        List<T> processedItems = new LinkedList<>();
        try {
            T item;
            while ((item = readerClone.read()) != null) {
                item = processorClone.proccess(item);
                processedItems.add(item);
            }
            writerClone.write(processedItems);

        } catch (Exception ex) {
            for (ChunkListener listener : clonedListeners) {
                listener.onFail(ex);
            }
            return;
        }

        //on finish
        for (ChunkListener listener : clonedListeners) {
            listener.onSuccess();
        }
    }

    public void addListener(Class<? extends ChunkListener> listener) {
        listeners.add(listener);
    }


}
