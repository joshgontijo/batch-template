package com.josue.distributed.job;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class MongoClientProvider {

    private MongoClient mongoClient;

    @PostConstruct
    public void init() {
        ServerAddress address = new ServerAddress("mongocontainer", 27017);
        mongoClient = new MongoClient(address);
    }

    @Produces
    public MongoClient produce() {
        return mongoClient;
    }

    @PreDestroy
    public void cleanup() {
        mongoClient.close();
    }
}
