package com.josue.batch.agent.core;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Josue Gontijo .
 */
public class BatchWorker implements Runnable {

    private boolean accept = true;
    private BlockingQueue<Properties> chunkQueue;
    private ChunkExecutorConfig executorConfig;
    private String id;

    public BatchWorker(BlockingQueue<Properties> chunkQueue, ChunkExecutorConfig executorConfig) {
        this.chunkQueue = chunkQueue;
        this.executorConfig = executorConfig;
        this.id = UUID.randomUUID().toString().substring(0, 4);
    }

    @Override
    public void run() {
        System.out.println(":: Started worker " + id + " ::");
        while (accept) {
            try {
                Properties properties = chunkQueue.poll(500, TimeUnit.MILLISECONDS);
                ChunkExecutor chunkExecutor = new ChunkExecutor<>(executorConfig, properties);
                chunkExecutor.execute();
            } catch (InterruptedException e) {
                //TODO improve this
                e.printStackTrace();
            }
        }
        System.out.println(":: Stopped worker " + id + " ::");
    }

    public void shutdown() {
        System.out.println(":: Shutdown request for " + id + " ::");
        this.accept = false;
    }

}
