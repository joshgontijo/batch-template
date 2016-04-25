package com.josue.batch.agent.core;

import com.josue.batch.agent.chunk.ChunkExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Josue Gontijo .
 */
public class BatchRuntime {

    private static BatchRuntime instance;
    private final BlockingQueue<ChunkExecutor<?>> chunkQueue = new ArrayBlockingQueue<>(100000);

    private static final int MAX_THREADS = 2;

    private ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
    private List<BatchWorker> managers = new ArrayList<>();


    private BatchRuntime() {
        System.out.println(":: INITIALISING WORKERS, WORKER COUNT: " + MAX_THREADS + " ::");
        for (int i = 0; i < MAX_THREADS; i++) {
            executor.execute(new BatchWorker(chunkQueue));
        }
    }

    public static BatchRuntime getInstance() {
        if (instance == null) {
            instance = new BatchRuntime();
        }
        return instance;
    }

    public void submit(ChunkExecutor chunkExecutor) {
        chunkQueue.add(chunkExecutor);
    }

    public void stop() {
        System.out.println("Sending shutdown signal...");
        for (BatchWorker bm : managers) {
            bm.shutdown();
        }
    }


}
