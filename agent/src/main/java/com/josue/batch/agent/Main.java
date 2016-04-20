package com.josue.batch.agent;

import com.josue.batch.agent.impl.SampleItemProcessor;
import com.josue.batch.agent.impl.SampleItemReader;
import com.josue.batch.agent.impl.SampleItemWriter;

/**
 * Created by Josue on 19/04/2016.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        BatchExecutor<String> executor = new BatchExecutor<>(
                new SampleItemReader(),
                new SampleItemProcessor(),
                new SampleItemWriter());

        executor.setThreads(100);
        executor.chunkSize(1000);
        executor.execute();

    }

}
