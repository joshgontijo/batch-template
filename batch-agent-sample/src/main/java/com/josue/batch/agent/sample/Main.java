package com.josue.batch.agent.sample;

import com.josue.batch.agent.core.BatchConfig;
import com.josue.batch.agent.core.BatchService;
import com.josue.batch.agent.core.ChunkExecutorConfig;

import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class Main {

    public static final String START = "start";
    public static final String END = "end";

    public static void main(String[] args) throws InterruptedException {

        ChunkExecutorConfig execConfig = new ChunkExecutorConfig(SampleReader.class, SampleWriter.class);
        execConfig.processor(SampleProcessor.class);
        execConfig.addListener(SampleListener.class);

        BatchService service = new BatchService(new BatchConfig(), execConfig);

        int itemPerChunk = 10;
        for (int i = 0; i < 10; i++) {
            Thread.sleep(500);

            Properties properties = new Properties();
            properties.setProperty(START, String.valueOf(i * itemPerChunk));
            properties.setProperty(END, String.valueOf((i * itemPerChunk) + itemPerChunk));

            service.submit(properties);
        }

        Thread.sleep(2000);

        service.stop();

        service.shutdown();

        Thread.sleep(2000);

//        BatchRuntime.getInstance().stop();
//        Thread.sleep(5000);

    }

}
