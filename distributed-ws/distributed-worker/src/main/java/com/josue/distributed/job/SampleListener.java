package com.josue.distributed.job;

import com.josue.batch.agent.core.ChunkListener;
import com.josue.batch.agent.metric.Meter;
import com.josue.distributed.ChunkEvent;

import javax.inject.Inject;
import javax.websocket.Session;
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
    private Session session;

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

        ChunkEvent chunkEvent = new ChunkEvent(masterId);
        chunkEvent.put("start", init);
        chunkEvent.put("end", end);
        chunkEvent.put("status", "DONE");

        session.getAsyncRemote().sendObject(chunkEvent);
    }

    @Override
    public void onFail(Exception ex) {
        String masterId = properties.getProperty("masterId");
        String init = properties.getProperty("start");
        String end = properties.getProperty("end");

        ChunkEvent chunkEvent = new ChunkEvent(masterId);
        chunkEvent.put("start", init);
        chunkEvent.put("end", end);
        chunkEvent.put("status", "FAILED");

        session.getAsyncRemote().sendObject(chunkEvent);
    }

}
