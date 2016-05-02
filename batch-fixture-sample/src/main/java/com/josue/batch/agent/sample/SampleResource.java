package com.josue.batch.agent.sample;

import com.josue.batch.agent.core.ChunkExecutor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Properties;
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

    @PostConstruct
    public void init(){

    }

    @GET
    @Produces("text/plain")
    public String getMessage(@QueryParam("start") @DefaultValue("1") String start,
                             @QueryParam("end") @DefaultValue("10") String end) {

        Properties properties = new Properties();
        properties.setProperty("start", start);
        properties.setProperty("end", end);

        stageChunkExecutor.submit(properties);
        return "Submited";
    }
}
