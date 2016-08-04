package com.josue.distributed;

import com.josue.batch.agent.metric.Metric;
import com.josue.distributed.config.PipelineStore;
import com.mongodb.MongoClient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by Josue on 27/04/2016.
 */

@Path("/chunks")
@ApplicationScoped
public class SampleResource {

    private static final Logger logger = Logger.getLogger(SampleResource.class.getName());

    @Inject
    private PipelineStore pipelineStore;

    @Inject
    private MongoClient mongoClient;

    @PostConstruct
    public void init() {
        LogManager.getLogManager().getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.mongodb.driver.management").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.mongodb.driver.cluster").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.mongodb.driver.protocol.insert").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.mongodb.driver.protocol.query").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.mongodb.driver.protocol.update").setLevel(Level.OFF);
    }

    @GET
    @Path("pipelines")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Metric> getMessage() {
        return pipelineStore.getMetrics();
    }



}
