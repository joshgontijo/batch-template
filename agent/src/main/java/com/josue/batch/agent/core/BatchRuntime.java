package com.josue.batch.agent.core;

import com.josue.batch.agent.chunk.ChunkExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Josue Gontijo <josue.gontijo@maersk.com>.
 */
public class BatchRuntime {

    private static final BatchRuntime instance = new BatchRuntime();
    private final BlockingQueue<ChunkExecutor<?>> chunkQueue = new ArrayBlockingQueue<>(1000);

    private static final int MAX_THREADS = 2;

    private ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
    private List<BatchManager> managers = new ArrayList<>();


    private BatchRuntime() {
        for (int i = 0; i < MAX_THREADS; i++) {
            managers.add(new BatchManager(chunkQueue));
        }
    }

    public static BatchRuntime getInstance() {
        return instance;
    }

    public void submit(ChunkExecutor chunkExecutor) {
        chunkQueue.add(chunkExecutor);
    }

    public void start() {
        for (BatchManager bm : managers) {
            executor.execute(bm);
        }
    }

    public void stop() {
        System.out.println("Sending shutdown signal...");
        for (BatchManager bm : managers) {
            bm.shutdown();
        }
    }


}
