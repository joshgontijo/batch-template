package com.josue.batch.agent.sample;

import com.josue.batch.agent.stage.StageChunkWriter;

import java.util.List;
import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleWriter extends StageChunkWriter<String> {

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
