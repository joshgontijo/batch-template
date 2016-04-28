package com.josue.batch.agent.sample;

import com.josue.batch.agent.core.ChunkListener;
import com.josue.batch.agent.core.Executor;
import com.josue.batch.agent.sample.fixture.SampleListener;
import com.josue.batch.agent.sample.fixture.SampleProcessor;
import com.josue.batch.agent.sample.fixture.SampleReader;
import com.josue.batch.agent.sample.fixture.SampleWriter;
import com.josue.batch.agent.stage.StageChunkExecutor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

/**
 * Created by Josue on 27/04/2016.
 */

@Path("/jobs")
@ApplicationScoped
public class SampleResource {

    private static final Logger logger = Logger.getLogger(SampleResource.class.getName());

    @Inject
    ExecutorService service;

    private Executor stageExecutor;

    @PostConstruct
    public void init(){
        List<Class<? extends ChunkListener>> listeners = new LinkedList<>();
        listeners.add(SampleListener.class);
        stageExecutor = new StageChunkExecutor<>(
                SampleReader.class,
                SampleProcessor.class,
                SampleWriter.class,
                listeners,
                service);
    }

    @GET
    @Produces("text/plain")
    public String getMessage(@QueryParam("start") @DefaultValue("1") String start,
                             @QueryParam("end") @DefaultValue("10") String end) {

        Properties properties = new Properties();
        properties.setProperty("start", start);
        properties.setProperty("end", end);

        stageExecutor.submit(properties);
        return "Submited";
    }
}
