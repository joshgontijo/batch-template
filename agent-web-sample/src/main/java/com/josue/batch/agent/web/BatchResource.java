package com.josue.batch.agent.web;

import com.josue.batch.agent.stage.StageExecutorConfig;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Properties;

/**
 * @author Josue Gontijo <josue.gontijo@maersk.com>.
 */
@Path("jobs")
@ApplicationScoped
public class BatchResource {

    @Resource
    private ManagedExecutorService service;

    private StageExecutorConfig execConfig;

    @PostConstruct
    public void init() {
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response startJob(Properties properties) {
        return Response.ok().build();
    }
}
