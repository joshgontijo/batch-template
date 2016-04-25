package com.josue.batch.agent.impl;

import com.josue.batch.agent.chunk.ChunkWriter;

import java.util.List;
import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleWriter extends ChunkWriter<String> {

    private Properties properties;

    @Override
    public void init(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void write(List<String> items) {
        System.out.println(":: ITEM SIZE " + items.size() + " ::");
        for (String item : items) {
//            System.out.println(" -> " + item);
        }
    }
}
