package com.josue.batch.agent.sample.fixture;

import com.josue.batch.agent.metric.Meter;
import com.josue.batch.agent.stage.StageChunkReader;

import javax.annotation.PostConstruct;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleReader extends StageChunkReader {

    private int current;
    private int end;

    private static final Logger logger = Logger.getLogger(SampleReader.class.getName());

    @PostConstruct
    public void init() {
        logger.info("SampleReader@PostConstruct");
    }

    @Override
    public void init(Properties properties, Meter meter) {
        current = Integer.valueOf(properties.getProperty("start"));
        end = Integer.valueOf(properties.getProperty("end"));
    }

    @Override
    public void close() {

    }

    @Override
    public String read() {
        if (current > end) {
            return null;
        }
        return String.valueOf(current++);
    }
}
