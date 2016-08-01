package com.josue.batch.agent.sample.fixture;

import com.josue.batch.agent.core.ChunkListener;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

/**
 * Created by Josue on 25/04/2016.
 */
public class SampleListener extends ChunkListener {

    private long start;

    private static final Logger logger = Logger.getLogger(SampleListener.class.getName());

    @PostConstruct
    public void init() {
        logger.info("SampleListener@PostConstruct");
    }

    @Override
    public void onStart() {
        start = System.currentTimeMillis();
    }

    @Override
    public void onSuccess() {
        System.out.println(":: FINISHED IN " + (System.currentTimeMillis() - start) + "ms ::");
    }

    @Override
    public void onFail(Exception ex) {

    }
}
