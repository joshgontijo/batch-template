package com.josue.distributed.job;

import com.josue.batch.agent.stage.StageChunkWriter;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Josue on 19/04/2016.
 */
public class SampleWriter extends StageChunkWriter {

    private Properties properties;

    private static final Logger logger = Logger.getLogger(SampleWriter.class.getName());
    private MongoClient mongoClient;
    private MongoCollection<Document> userCollection;

    @Override
    public void init(Properties properties) {
        this.properties = properties;

        mongoClient = new MongoClient("localhost", 27017);
        userCollection = mongoClient.getDatabase("sample").getCollection("user");
    }

    @Override
    public void write(List<Object> items) {

        List<Document> users = new ArrayList<>();
        for(Object item : items){
            Map<String, Object> line = (Map<String, Object>) item;
            Document document = new Document();
            for(Map.Entry<String, Object> entry : line.entrySet()){
                document.append(entry.getKey(), entry.getValue());
            }
            users.add(document);
        }

        userCollection.insertMany(users);

    }

    @Override
    public void close() throws Exception {
        mongoClient.close();
    }
}
