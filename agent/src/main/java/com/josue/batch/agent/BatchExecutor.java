package com.josue.batch.agent;

import org.apache.commons.lang3.SerializationUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Josue on 19/04/2016.
 */
public class BatchExecutor<T> {

    private final ItemReader<T> reader;
    private final ItemProcessor<T> processor;
    private final ItemWriter<T> writer;

    private int chunkSize = 10;

    private final ExecutorService service = Executors.newFixedThreadPool(10);
    private int threads = 2;

    public BatchExecutor(ItemReader<T> reader, ItemProcessor<T> processor, ItemWriter<T> writer) {
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
    }

    public void execute() {
        for (int i = 0; i < threads; i++) {
            Properties props = new Properties();
            props.setProperty("start", String.valueOf(i * 10 + 1));
            props.setProperty("end", String.valueOf((i + 1) * 10));
            props.setProperty("id", String.valueOf(i));

            ItemReader<T> readerClone = SerializationUtils.clone(reader);
            readerClone.init(props);

            ItemProcessor<T> processorClone = SerializationUtils.clone(processor);
            processorClone.init(props);

            ItemWriter<T> writerClone = SerializationUtils.clone(writer);
            writerClone.init(props);

            service.execute(new BatchService(readerClone, processorClone, writerClone));
        }

        try {
            service.shutdown();
            service.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void setThreads(int threads) {
        this.threads = threads;
    }

    public void chunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    class BatchService implements Runnable {

        private final ItemReader<T> reader;
        private final ItemProcessor<T> processor;
        private final ItemWriter<T> writer;

        BatchService(ItemReader<T> reader, ItemProcessor<T> processor, ItemWriter<T> writer) {
            this.reader = reader;
            this.processor = processor;
            this.writer = writer;
        }

        @Override
        public void run() {
            List<T> processedItems = new LinkedList<>();

            T item;
            while ((item = this.reader.read()) != null) {
                item = this.processor.proccess(item);
                processedItems.add(item);
                if (processedItems.size() == chunkSize) {
                    this.writer.write(processedItems);
                    processedItems = new LinkedList<>();
                }
            }
            if (!processedItems.isEmpty()) {
                this.writer.write(processedItems);
            }
        }
    }

}
