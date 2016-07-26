package com.josue.distributed.event;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.josue.distributed.JobEvent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Josue on 26/07/2016.
 */
@ApplicationScoped
public class FairJobStore {

    private static final String JOBS_MAP_PREFIX = "JOBS_";
    private static final String BACKUP_MAP_PREFIX = "BKP_";

    private static final Object LOCK = new Object();

    Map<String, Queue<JobEvent>> jobs;
    Map<String, JobEvent> backup;

    private Iterator<Map.Entry<String, Queue<JobEvent>>> jobsIterator;

    @Inject
    private JobWatcher listener;

    @PostConstruct
    public void init() {
        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance();
        String uuid = hazelcast.getCluster().getLocalMember().getUuid();

        jobs = hazelcast.getMap(JOBS_MAP_PREFIX + uuid);
        backup = hazelcast.getMap(BACKUP_MAP_PREFIX + uuid);
    }

    public JobEvent get() {
        synchronized (LOCK) {
            if (jobs.isEmpty()) {
                return null;
            }
            if (jobsIterator == null || !jobsIterator.hasNext()) {
                jobsIterator = jobs.entrySet().iterator();
            }
            Queue<JobEvent> queue = jobsIterator.next().getValue();
            if (queue.isEmpty()) {
                jobsIterator.remove();
                this.get();
            }
            JobEvent jobEvent = queue.poll();
            if (jobEvent != null) {
                backup.put(jobEvent.getId(), jobEvent);
            }
            return jobEvent;
        }
    }

    public void add(JobEvent jobEvent) {
        if (jobEvent == null
                || jobEvent.getMasterId() == null
                || jobEvent.getMasterId().isEmpty()) {
            return;
        }
        synchronized (LOCK) {
            if (!jobs.containsKey(jobEvent.getMasterId())) {
                jobs.put(jobEvent.getMasterId(), new ConcurrentLinkedQueue<>());
            }
            jobs.get(jobEvent.getMasterId()).add(jobEvent);
        }
    }

    public JobEvent releaseJob(String id) {
        return backup.remove(id);
    }


}
