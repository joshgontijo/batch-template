package com.josue.distributed.event;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.batch.agent.core.Statistic;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * Created by Josue on 26/07/2016.
 */
@ApplicationScoped
public class JobWatcher {

    @Resource
    private ManagedScheduledExecutorService mses;

    @Inject
    private ChunkExecutor executor;

    @Inject
    private FairJobStore store;

    @PostConstruct
    public void init() {
        mses.schedule((Runnable) () -> {
            Statistic statistic = executor.getStatistic();

            if (statistic.getActiveCount() < statistic.getMaximumPoolSize() && store.hasJobs()) {

            }
        }, 1000, TimeUnit.SECONDS);

    }


}
