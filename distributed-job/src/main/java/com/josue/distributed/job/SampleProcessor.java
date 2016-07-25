package com.josue.distributed.job;

import au.com.bytecode.opencsv.CSVReader;
import com.josue.batch.agent.stage.StageChunkProcessor;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleProcessor extends StageChunkProcessor {

    private static final Logger logger = Logger.getLogger(SampleProcessor.class.getName());


    private String[] headers;

    @Override
    public void init(Properties properties) throws Exception {
        String fileName = properties.getProperty("fileName");
        InputStreamReader inputStreamReader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(fileName));
        CSVReader reader = new CSVReader(inputStreamReader);
        headers = reader.readNext();
    }

    @Override
    public Map<String, Object> proccess(Object input) {
        String[] values = (String[]) input;

        Map<String, Object> line = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            line.put(headers[i], values[i]);
        }

        return line;

    }
}
