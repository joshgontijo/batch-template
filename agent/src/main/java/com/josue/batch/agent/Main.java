package com.josue.batch.agent;

import com.josue.batch.agent.chunk.ChunkExecutor;
import com.josue.batch.agent.chunk.ChunkListener;
import com.josue.batch.agent.core.BatchRuntime;
import com.josue.batch.agent.impl.SampleChunkProcessor;
import com.josue.batch.agent.impl.SampleChunkReader;
import com.josue.batch.agent.impl.SampleChunkWriter;

/**
 * Created by Josue on 19/04/2016.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        ChunkExecutor<String> executor = new ChunkExecutor<>(
                new SampleChunkReader(),
                new SampleChunkProcessor(),
                new SampleChunkWriter());


        executor.addListener(new ChunkListener() {
            @Override
            protected void onStart() {
                System.out.println("onStart");
            }

            @Override
            protected void onSuccess() {
                System.out.println("onSuccess");
            }

            @Override
            protected void onFail(Exception ex) {
                System.err.println("onFail -> " + ex.getMessage());
            }
        });

        for (int i = 0; i < 1000; i++) {
            BatchRuntime.getInstance().submit(executor);
        }

        BatchRuntime.getInstance().start();
        Thread.sleep(5000);

//        BatchRuntime.getInstance().stop();
//        Thread.sleep(5000);

    }

}
