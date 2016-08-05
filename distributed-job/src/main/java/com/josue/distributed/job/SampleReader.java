package com.josue.distributed.job;

import au.com.bytecode.opencsv.CSVReader;
import com.josue.batch.agent.metric.Meter;
import com.josue.batch.agent.stage.StageChunkReader;

import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleReader extends StageChunkReader {

    private static final Logger logger = Logger.getLogger(SampleReader.class.getName());

    private final AtomicInteger current = new AtomicInteger();
    private int total;

    private CSVReader reader;

    @Override
    public void init(Properties properties, Meter meter) throws Exception {
        Integer start = Integer.valueOf(properties.getProperty("start"));
        Integer end = Integer.valueOf(properties.getProperty("end"));
        String fileName = properties.getProperty("fileName");

        if (start == null) {
            throw new RuntimeException("'start' prperty not provided");
        }
        if (end == null) {
            throw new RuntimeException("'end' prperty not provided");
        }
        if (fileName == null) {
            throw new RuntimeException("'fileName' prperty not provided");
        }

        total = (end - start);


        InputStreamReader inputStreamReader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(fileName));
        reader = new CSVReader(inputStreamReader, ',', '\'', start + 1); //1 - exclude header
    }

    @Override
    public void close() {

    }

    @Override
    public String[] read() throws Exception {
        if (current.incrementAndGet() > total) {
            return null; //end
        }
        String[] strings = reader.readNext();

        return strings;
    }
}
