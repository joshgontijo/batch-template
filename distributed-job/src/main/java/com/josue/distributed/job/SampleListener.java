package com.josue.distributed.job;

import com.josue.batch.agent.core.ChunkListener;
import com.josue.batch.agent.metric.Meter;
import com.josue.distributed.event.FairJobStore;

import javax.inject.Inject;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Josue on 25/04/2016.
 */
public class SampleListener extends ChunkListener {

    private long start;

    private static final Logger logger = Logger.getLogger(SampleListener.class.getName());
    private Properties properties;

    @Inject
    private FairJobStore store;

    @Override
    public void init(Properties properties, Meter meter) throws Exception {
        this.properties = properties;
    }

    @Override
    public void onStart() {
        start = System.currentTimeMillis();
    }

    @Override
    public void onSuccess() {

        String masterId = properties.getProperty("masterId");
        String init = properties.getProperty("start");
        String end = properties.getProperty("end");
        logger.info(":: JOB " + masterId + " (start: " + init + ", end: " + end + " FINISHED IN " + (System.currentTimeMillis() - start) + "ms ::");

        store.releaseJob(properties.getProperty("id"));
    }

    @Override
    public void onFail(Exception ex) {
        store.releaseJob(properties.getProperty("id"));
    }

}
