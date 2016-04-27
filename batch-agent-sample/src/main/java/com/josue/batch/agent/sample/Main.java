package com.josue.batch.agent.sample;

import com.josue.batch.agent.core.BatchConfig;
import com.josue.batch.agent.core.BatchService;
import com.josue.batch.agent.stage.StageExecutorConfig;

import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class Main {

    public static final String START = "start";
    public static final String END = "end";

    public static void main(String[] args) throws InterruptedException {

        StageExecutorConfig execConfig = new StageExecutorConfig(SampleReader.class, SampleWriter.class);
        execConfig.processor(SampleProcessor.class);
        execConfig.addListener(SampleListener.class);

        BatchService service = new BatchService(new BatchConfig(20, 10), execConfig);

        int itemPerChunk = 10;
        for (int i = 0; i < 10; i++) {

            Properties properties = new Properties();
            properties.setProperty(START, String.valueOf(i * itemPerChunk));
            properties.setProperty(END, String.valueOf((i * itemPerChunk) + itemPerChunk));

            service.submit(properties);
        }

        System.out.println("Main Thread: Waiting 10s to shutdown");
        Thread.sleep(10000);

        System.out.println("Main Thread: shutting down");
        service.stop();
        service.shutdown();

//        BatchRuntime.getInstance().stop();

    }

}
