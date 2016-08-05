package com.josue.batch.agent.sample.fixture;

import com.josue.batch.agent.metric.Meter;
import com.josue.batch.agent.stage.StageChunkWriter;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleWriter extends StageChunkWriter {

    private Properties properties;

    private static final Logger logger = Logger.getLogger(SampleWriter.class.getName());

    @PostConstruct
    public void init() {
        logger.info("SampleWriter@PostConstruct");
    }

    @Override
    public void init(Properties properties, Meter meter) {
        this.properties = properties;
    }

    @Override
    public void write(List<Object> items) {
        System.out.println(":: ITEM SIZE " + items.size() + " ::");
        for (Object item : items) {
//            System.out.println(" -> " + item);
        }
    }
}
