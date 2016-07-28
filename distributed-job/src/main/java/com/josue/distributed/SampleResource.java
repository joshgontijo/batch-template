package com.josue.distributed;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.distributed.event.FairJobStore;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
    private ChunkExecutor stageChunkExecutor;

    @Inject
    private FairJobStore store;

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
    @Produces("text/plain")
    public String getMessage(@QueryParam("numJobs") @DefaultValue("1000") Integer numJobs) {


        //TODO implement job splitter (when odd item count, add a job with the remaining)
        int csvEntryCount = 1000000;

//        Properties properties = new Properties();
//        properties.setProperty("start", String.valueOf(0));
//        properties.setProperty("end", String.valueOf(1000000));
//        properties.setProperty("fileName", "majestic_million.csv");
//        properties.setProperty("id", String.valueOf(1));
//
//        stageChunkExecutor.submitChunk(properties);


        List<JobEvent> events = new ArrayList<>();

        String jobId = "job-a";
        int itemPerJob = csvEntryCount / numJobs;
        for (int i = 0; i < numJobs; i++) {

            int start = (i * itemPerJob);

            Properties properties = new Properties();
            properties.setProperty("start", String.valueOf(start));
            properties.setProperty("end", String.valueOf(start + itemPerJob));
            properties.setProperty("fileName", "majestic_million.csv");
            properties.setProperty("id", String.valueOf(i));

            events.add(new JobEvent(jobId, "majestic_million.csv", start, start + itemPerJob));
        }

        store.add(events);

        return "Submited";
    }

//    private <T> List<List<T>> splitList(List<T> list, final int L) {
//        List<List<T>> parts = new ArrayList<>();
//        final int N = list.size();
//        for (int i = 0; i < N; i += L) {
//            parts.add(new ArrayList<>(
//                    list.subList(i, Math.min(N, i + L)))
//            );
//        }
//        return parts;
//    }
}
