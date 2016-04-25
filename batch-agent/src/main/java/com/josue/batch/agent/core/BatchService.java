package com.josue.batch.agent.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by Josue on 25/04/2016.
 */
public class BatchService {

    private final BatchConfig config;
    private final ChunkExecutorConfig executorConfig;
    private final List<BatchWorker> managers = new ArrayList<>();


    public BatchService(BatchConfig config, ChunkExecutorConfig executorConfig) {
        this.config = config;
        this.executorConfig = executorConfig;

        for (int i = 0; i < config.getExecutor().getMaximumPoolSize(); i++) {
            BatchWorker batchWorker = new BatchWorker(config.getQueue(), executorConfig);
            managers.add(batchWorker);
            config.getExecutor().execute(batchWorker);
        }

    }

    public void submit(Properties properties) {
        config.getQueue().add(properties);
    }

    public void stop() {
        System.out.println("Sending shutdown signal...");
        for (BatchWorker bm : managers) {
            bm.shutdown();
        }
    }

    public void shutdown() {
        config.getExecutor().shutdown();
    }

    public void waitFor() throws InterruptedException {
        config.getExecutor().awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

}
