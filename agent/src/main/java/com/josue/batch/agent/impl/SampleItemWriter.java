package com.josue.batch.agent.impl;

import com.josue.batch.agent.ItemWriter;

import java.util.List;
import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleItemWriter extends ItemWriter<String> {

    private Properties properties;

    @Override
    public void init(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void write(List<String> items) {
        System.out.println(":: ITEM SIZE " + items.size() + " ::");
        for (String item : items) {
            System.out.println("ID:" + properties.getProperty("id") + " -> " + item);
        }
    }
}
