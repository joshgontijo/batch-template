package com.josue.batch.agent.impl;

import com.josue.batch.agent.chunk.ChunkProcessor;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleProcessor extends ChunkProcessor<String> {

    @Override
    public String proccess(String input) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Processed: " + input);
        return input + " -> OK";
    }
}
