package com.josue.distributed.event;

import com.hazelcast.core.HazelcastInstance;
import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.distributed.JobEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Properties;

/**
 * Created by Josue on 26/07/2016.
 */
@ApplicationScoped
public class JobManager {

    @Inject
    private ChunkExecutor executor;

    @Inject
    private HazelcastInstance hazelcast;

    public void submitChunk(JobEvent jobEvent) {
        if (jobEvent == null) {
            return;
        }
        Properties props = new Properties();
        props.setProperty("start", String.valueOf(jobEvent.getStart()));
        props.setProperty("id", String.valueOf(jobEvent.getId()));
        props.setProperty("end", String.valueOf(jobEvent.getEnd()));
        props.setProperty("fileName", jobEvent.getFileName());
        executor.submit(props);
    }

    public boolean hasRunningJob(){
        return executor.getStatistic().getActiveCount() > 0;
    }


}
