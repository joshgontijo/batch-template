package com.josue.distributed.job;

import com.josue.batch.agent.core.ChunkListener;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Josue on 25/04/2016.
 */
public class SampleListener extends ChunkListener {

    private long start;

    private static final Logger logger = Logger.getLogger(SampleListener.class.getName());
    private Properties properties;

    @Override
    public void init(Properties properties) throws Exception {
        this.properties = properties;
    }

    @Override
    public void onStart() {
        start = System.currentTimeMillis();
    }

    @Override
    public void onSuccess() {

        String id = properties.getProperty("id");
        String init = properties.getProperty("start");
        String end = properties.getProperty("end");
        System.out.println(":: JOB " + id + " (start: " + init + ", end: " + end + " FINISHED IN " + (System.currentTimeMillis() - start) + "ms ::");
    }

    @Override
    public void onFail(Exception ex) {

    }
}
