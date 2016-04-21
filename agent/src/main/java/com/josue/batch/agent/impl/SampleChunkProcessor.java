package com.josue.batch.agent.impl;

import com.josue.batch.agent.chunk.ChunkProcessor;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleChunkProcessor extends ChunkProcessor<String> {
    @Override
    public String proccess(String input) {
        return input + " -> OK";
    }
}
