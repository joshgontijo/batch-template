package com.josue.batch.agent.impl;

import com.josue.batch.agent.chunk.ChunkReader;

import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleChunkReader extends ChunkReader<String> {

    private Properties properties;
    private int current;

    @Override
    public void init(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String read() {
        if(current == 100){
            return null;
        }
        return String.valueOf(current++);
    }
}
