package com.josue.batch.agent.sample;

import com.josue.batch.agent.stage.StageChunkReader;

import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleReader extends StageChunkReader<String> {

    private Properties properties;
    private int current;
    private int end;

    public SampleReader() {

    }

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
