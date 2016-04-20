package com.josue.batch.agent.impl;

import com.josue.batch.agent.ItemProcessor;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleItemProcessor extends ItemProcessor<String> {
    @Override
    public String proccess(String input) {
        return input + " -> OK";
    }
}
