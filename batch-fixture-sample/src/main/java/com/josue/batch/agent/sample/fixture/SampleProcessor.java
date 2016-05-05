package com.josue.batch.agent.sample.fixture;

import com.josue.batch.agent.stage.StageChunkProcessor;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleProcessor extends StageChunkProcessor {

    private static final Logger logger = Logger.getLogger(SampleProcessor.class.getName());

    @PostConstruct
    public void init() {
        logger.info("SampleListener@PostConstruct");
    }

    @Override
    public String proccess(Object input) {
        try {
            Thread.sleep(new Random().nextInt((1000 - 200) + 1) + 200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Processed item value: " + input + "... thread id: " + Thread.currentThread().getId());
        return input + " -> OK";
    }
}
