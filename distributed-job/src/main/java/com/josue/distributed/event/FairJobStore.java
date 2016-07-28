package com.josue.distributed.event;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import com.hazelcast.core.Member;
import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.distributed.JobEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Josue on 26/07/2016.
 */
@ApplicationScoped
public class FairJobStore implements ItemListener<JobEvent> {

    private static final String JOBS_MAP_PREFIX = "JOBS_";
    private static final String BACKUP_MAP_PREFIX = "BKP_";

    Queue<JobEvent> jobs;
    Map<String, JobEvent> backup;

    @Inject
    private JobManager jobManager;

    @Inject
    private ChunkExecutor executor;

    @Inject
    private HazelcastInstance hazelcast;

    @PostConstruct
    public void init() {
        String uuid = hazelcast.getCluster().getLocalMember().getUuid();

        jobs = hazelcast.getQueue(JOBS_MAP_PREFIX + uuid);
        backup = hazelcast.getMap(BACKUP_MAP_PREFIX + uuid);

        ((IQueue) jobs).addItemListener(this, true);

        if (hasJobs()) {
            JobEvent jobEvent = get();
            jobManager.submitChunk(jobEvent);
        }
    }

    public JobEvent get() {
        if (jobs.isEmpty()) {
            return null;
        }
        JobEvent jobEvent = jobs.poll();
        if (jobEvent != null) {
            backup.put(jobEvent.getId(), jobEvent);
        }
        return jobEvent;
    }

    public void add(List<JobEvent> jobEvents) {
        Set<Member> members = hazelcast.getCluster().getMembers();
        Iterator<Member> iterator = members.iterator();
        for (JobEvent event : jobEvents) {
            if (!iterator.hasNext()) {
                iterator = members.iterator();
            }
            Member member = iterator.next();

            IQueue<JobEvent> distQueue = hazelcast.getQueue(JOBS_MAP_PREFIX + member.getUuid());
            distQueue.add(event);
        }
    }


    public JobEvent releaseJob(String id) {
        return backup.remove(id);
    }

    public boolean hasJobs() {
        return !jobs.isEmpty();
    }


    @Override
    public void itemAdded(ItemEvent<JobEvent> itemEvent) {
        if (!jobManager.hasRunningJob()) {
            jobManager.submitChunk(get());
        }
    }

    @Override
    public void itemRemoved(ItemEvent<JobEvent> itemEvent) {

    }
}
