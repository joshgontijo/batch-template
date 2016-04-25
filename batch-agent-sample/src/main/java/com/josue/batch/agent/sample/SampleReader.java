package com.josue.batch.agent.sample;

import com.josue.batch.agent.chunk.ChunkReader;

import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleReader extends ChunkReader<String> {

    private Properties properties;
    private int current;
    private int end;

    @Override
    public void init(Properties properties) {
        this.properties = properties;
        current = Integer.valueOf(properties.getProperty(Main.START));
        end = Integer.valueOf(properties.getProperty(Main.END));
    }

    @Override
    public String read() {
        if (current == end) {
            return null;
        }
        return String.valueOf(current++);
    }
}
