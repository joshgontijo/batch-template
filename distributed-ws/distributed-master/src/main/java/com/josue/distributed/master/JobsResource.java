package com.josue.distributed.master;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by Josue on 02/08/2016.
 */
// The Java class will be hosted at the URI path "/helloworld"
@Path("/jobs")
public class JobsResource {

    @Inject
    private JobEndpoint serverEndpoint;

    @GET
    @Produces("text/plain")
    public String getMessage(@QueryParam("numJobs") @DefaultValue("1000") Integer numJobs) {

        //TODO implement job splitter (when odd item count, add a job with the remaining)
        int csvEntryCount = 1000000;

        List<ChunkEvent> events = new ArrayList<>();

        String jobId = UUID.randomUUID().toString().substring(0, 4);
        int itemPerJob = csvEntryCount / numJobs;
        for (int i = 0; i < numJobs; i++) {

            int start = (i * itemPerJob);

            Properties properties = new Properties();
            properties.setProperty("start", String.valueOf(start));
            properties.setProperty("end", String.valueOf(start + itemPerJob));
            properties.setProperty("fileName", "majestic_million.csv");
            properties.setProperty("id", String.valueOf(i));

            ChunkEvent ChunkEvent = new ChunkEvent(jobId);
            ChunkEvent.put("start", String.valueOf(start));
            ChunkEvent.put("end", String.valueOf(start + itemPerJob));
            ChunkEvent.put("fileName", "majestic_million.csv");

            events.add(ChunkEvent);
        }

        for (ChunkEvent event : events) {
            serverEndpoint.startChunk(event);
        }

        return "Submited";
    }

}
