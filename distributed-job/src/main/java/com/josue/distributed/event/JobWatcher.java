package com.josue.distributed.event;

import com.josue.batch.agent.core.ChunkExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Created by Josue on 26/07/2016.
 */
@ApplicationScoped
public class JobWatcher {

    private static boolean running = false;

    @Resource
    private ManagedScheduledExecutorService mses;

    @Inject
    private ChunkExecutor executor;

    @PostConstruct
    public void init() {
        executor.getStatistic().

    }


}
