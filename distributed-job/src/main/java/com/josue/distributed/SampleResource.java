package com.josue.distributed;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.distributed.event.FairJobStore;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by Josue on 27/04/2016.
 */

@Path("/jobs")
@ApplicationScoped
public class SampleResource {

    private static final Logger logger = Logger.getLogger(SampleResource.class.getName());

    @Inject
    private FairJobStore store;

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
    public Map<String, ChunkExecutor> getMessage() {
        return pipelineStore.getPipelines();
    }

    @GET
    @Produces("text/plain")
    public String getMessage(@QueryParam("numJobs") @DefaultValue("1000") Integer numJobs) {

        try {

            for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
                System.out.println(entry.getKey() + "=" + entry.getValue());
            }

            MongoDatabase user = mongoClient.getDatabase("user");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //TODO implement job splitter (when odd item count, add a job with the remaining)
        int csvEntryCount = 1000000;

        List<JobEvent> events = new ArrayList<>();

        String jobId = UUID.randomUUID().toString();
        int itemPerJob = csvEntryCount / numJobs;
        for (int i = 0; i < numJobs; i++) {

            int start = (i * itemPerJob);

            Properties properties = new Properties();
            properties.setProperty("start", String.valueOf(start));
            properties.setProperty("end", String.valueOf(start + itemPerJob));
            properties.setProperty("fileName", "majestic_million.csv");
            properties.setProperty("id", String.valueOf(i));

            JobEvent jobEvent = new JobEvent(jobId);
            jobEvent.put("start", String.valueOf(start));
            jobEvent.put("end", String.valueOf(start + itemPerJob));
            jobEvent.put("fileName", "majestic_million.csv");

            events.add(jobEvent);
        }

        store.add(events);

        return "Submited";
    }

}
