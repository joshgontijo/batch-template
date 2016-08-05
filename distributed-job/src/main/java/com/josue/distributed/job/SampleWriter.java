package com.josue.distributed.job;

import com.josue.batch.agent.metric.Meter;
import com.josue.batch.agent.stage.StageChunkWriter;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleWriter extends StageChunkWriter {

    private static final Logger logger = Logger.getLogger(SampleWriter.class.getName());

    @Inject
    private MongoClient client;

    private MongoCollection<Document> userCollection;

    @Override
    public void init(Properties properties, Meter meter) {
        userCollection = client.getDatabase("sample").getCollection("user");
    }

    @Override
    public void write(List<Object> items) {
        if(items.isEmpty()){
            logger.warning("::: ITEMS IS EMPTY :::");
            return;
        }

        List<Document> users = new ArrayList<>();
        for (Object item : items) {
            Map<String, Object> line = (Map<String, Object>) item;
            Document document = new Document();
            for (Map.Entry<String, Object> entry : line.entrySet()) {
                document.append(entry.getKey(), entry.getValue());
            }
            users.add(document);
        }

        userCollection.insertMany(users);

    }
}
