package com.josue.batch.agent.impl;

import com.josue.batch.agent.chunk.ChunkListener;

/**
 * Created by Josue on 25/04/2016.
 */
public class SampleListener extends ChunkListener{

    private long start;


    @Override
    protected void onStart() {
        start = System.currentTimeMillis();
    }

    @Override
    protected void onSuccess() {
        System.out.println(":: FINISHED IN " + (System.currentTimeMillis() - start) + "ms ::");
    }

    @Override
    protected void onFail(Exception ex) {

    }
}
