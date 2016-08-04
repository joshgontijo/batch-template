package com.josue.distributed.job;

import com.josue.batch.agent.metric.Meter;
import com.josue.batch.agent.stage.StageChunkReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
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

    private CSVParser parser;
    private Iterator<CSVRecord> iterator;


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

        meter.start("openStream");
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(fileName)));
        meter.end("openStream");

        meter.start("skipLine");
        for (int i = 0; i < start; i++) {//header included
            reader.readLine();
        }
        meter.end("skipLine");

        parser = new CSVParser(reader, CSVFormat.DEFAULT);
        iterator = parser.iterator();
    }

    @Override
    public void close() {
        try {
            parser.close();
        } catch (IOException e) {
            //do nothing
        }
    }

    @Override
    public Map<String, String> read() throws Exception {
        if (current.incrementAndGet() > total || !iterator.hasNext()) {
            return null; //end
        }
        return iterator.next().toMap();
    }
}
