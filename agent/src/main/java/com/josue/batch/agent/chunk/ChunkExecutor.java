package com.josue.batch.agent.chunk;

import org.apache.commons.lang3.SerializationUtils;

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

    private boolean stopRequest;

    public ChunkExecutor(ChunkReader<T> reader, ChunkProcessor<T> processor, ChunkWriter<T> writer) {
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
    }

    public void execute() {
        execute(null);
    }

    public void execute(Properties props) {
        ChunkReader<T> readerClone = SerializationUtils.clone(reader);
        readerClone.init(props);

        ChunkProcessor<T> processorClone = SerializationUtils.clone(processor);
        processorClone.init(props);

        ChunkWriter<T> writerClone = SerializationUtils.clone(writer);
        writerClone.init(props);


        final List<ChunkListener> clonedListeners = new LinkedList<>();
        for (ChunkListener listener : listeners) {
            ChunkListener clonedList = SerializationUtils.clone(listener);
            clonedList.init(props);
            clonedListeners.add(clonedList);
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

//            if (stopRequest) {
//                //on finish
//                for (ChunkListener listener : clonedListeners) {
//                    listener.onStop();
//                }
//            }

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

    private void runJob() {

    }

    public void addListener(ChunkListener listener) {
        listeners.add(listener);
    }

    public void stop() {
        stopRequest = true;
    }

}
