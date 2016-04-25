package com.josue.batch.agent;

import com.josue.batch.agent.core.BatchConfig;
import com.josue.batch.agent.core.BatchService;
import com.josue.batch.agent.core.ChunkExecutorConfig;
import com.josue.batch.agent.impl.SampleListener;
import com.josue.batch.agent.impl.SampleProcessor;
import com.josue.batch.agent.impl.SampleReader;
import com.josue.batch.agent.impl.SampleWriter;

import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {

        ChunkExecutorConfig execConfig = new ChunkExecutorConfig(SampleReader.class, SampleWriter.class);
        execConfig.processor(SampleProcessor.class);
        execConfig.addListener(SampleListener.class);

        BatchService service = new BatchService(new BatchConfig(), execConfig);


        service.submit(new Properties());

//        for (int i = 0; i < 3; i++) {
//            Thread.sleep(500);
//            BatchRuntime.getInstance().submit(executor);
//        }

        Thread.sleep(2000);

        service.stop();

        service.shutdown();

        Thread.sleep(2000);

//        BatchRuntime.getInstance().stop();
//        Thread.sleep(5000);

    }

}
