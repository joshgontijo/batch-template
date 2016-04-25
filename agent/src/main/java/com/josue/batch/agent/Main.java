package com.josue.batch.agent;

import com.josue.batch.agent.chunk.ChunkExecutor;
import com.josue.batch.agent.core.BatchRuntime;
import com.josue.batch.agent.impl.SampleChunkProcessor;
import com.josue.batch.agent.impl.SampleChunkReader;
import com.josue.batch.agent.impl.SampleChunkWriter;
import com.josue.batch.agent.impl.SampleListener;

/**
 * Created by Josue on 19/04/2016.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        ChunkExecutor<String> executor = new ChunkExecutor<>(SampleChunkReader.class, SampleChunkProcessor.class, SampleChunkWriter.class);
        executor.addListener(SampleListener.class);

        for (int i = 0; i < 10000; i++) {
            Thread.sleep(500);
            BatchRuntime.getInstance().submit(executor);
        }

        Thread.sleep(5000);

//        BatchRuntime.getInstance().stop();
//        Thread.sleep(5000);

    }

}
