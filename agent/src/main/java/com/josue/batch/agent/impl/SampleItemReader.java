package com.josue.batch.agent.impl;

import com.josue.batch.agent.ItemReader;

import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleItemReader extends ItemReader<String> {

    private Properties properties;
    private int current;

    @Override
    public void init(Properties properties) {
        this.properties = properties;
        current = Integer.valueOf(properties.getProperty("start"));
    }

    @Override
    public String read() {
        int max = Integer.valueOf(properties.getProperty("end"));
        if(current <= max){
            return String.valueOf(current++);
        }
        return null;
    }
}
