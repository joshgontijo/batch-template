package com.josue.batch.agent.sample;

import com.josue.batch.agent.chunk.ChunkProcessor;

import java.util.Random;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleProcessor extends ChunkProcessor<String> {

    @Override
    public String proccess(String input) {
        try {
            Thread.sleep(new Random().nextInt((5 - 1) + 1) + 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Processed: " + input);
        return input + " -> OK";
    }
}
