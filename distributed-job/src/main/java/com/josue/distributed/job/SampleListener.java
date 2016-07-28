package com.josue.distributed.job;

import com.josue.batch.agent.core.ChunkListener;
import com.josue.distributed.JobEvent;
import com.josue.distributed.event.FairJobStore;
import com.josue.distributed.event.JobManager;

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

    @Inject
    private JobManager jobManager;


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

        triggerJob();
    }

    @Override
    public void onFail(Exception ex) {
        triggerJob();
    }

    private void triggerJob() {
        store.releaseJob(properties.getProperty("id"));
        if (store.hasJobs()) {
            JobEvent jobEvent = store.get();
            jobManager.submitChunk(jobEvent);
        }

    }
}
