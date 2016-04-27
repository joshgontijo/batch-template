package com.josue.batch.agent.sample;

import com.josue.batch.agent.stage.StageChunkProcessor;

import java.util.Random;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleProcessor extends StageChunkProcessor<String> {

    @Override
    public String proccess(String input) {
        try {
            Thread.sleep(new Random().nextInt((1000 - 200) + 1) + 200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Processed item value: " + input + "... thread id: " + Thread.currentThread().getId());
        return input + " -> OK";
    }
}
