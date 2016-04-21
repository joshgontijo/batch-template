package com.josue.batch.agent.core;

import com.josue.batch.agent.chunk.ChunkExecutor;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Josue Gontijo <josue.gontijo@maersk.com>.
 */
public class BatchManager implements Runnable {

    private boolean accept = true;
    private BlockingQueue<ChunkExecutor<?>> chunkQueue;
    private String id;

    public BatchManager(BlockingQueue<ChunkExecutor<?>> chunkQueue) {
        this.chunkQueue = chunkQueue;
        this.id = UUID.randomUUID().toString().substring(0, 4);
    }

    @Override
    public void run() {
        while (accept) {
            try {
                ChunkExecutor<?> chunk = chunkQueue.poll(100, TimeUnit.MILLISECONDS);
                if (chunk != null) {
                    chunk.execute();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Closed > " + id);
    }

    public void shutdown() {
        System.out.println("Shutdown request for > " + id);
        this.accept = false;
    }

}
