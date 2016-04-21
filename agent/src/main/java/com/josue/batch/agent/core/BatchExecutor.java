package com.josue.batch.agent.core;

import com.josue.batch.agent.chunk.ChunkExecutor;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Josue Gontijo <josue.gontijo@maersk.com>.
 */
public class BatchExecutor {

    private static final BatchExecutor instance = new BatchExecutor();
    private static final Map<String, Queue<ChunkExecutor<?>>> queues = new ConcurrentHashMap<>();

    private BatchExecutor() {
    }

    public static BatchExecutor getInstance() {
        return instance;
    }

    public void add(String jobUuid, ChunkExecutor executor) {
        if (!queues.containsKey(jobUuid)) {
            queues.put(jobUuid, new ConcurrentLinkedQueue<ChunkExecutor<?>>());
        }
        queues.get(jobUuid).add(executor);
    }


}
