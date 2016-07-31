package com.josue.distributed.event;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.Member;
import com.josue.distributed.JobEvent;
import com.josue.distributed.PipelineStore;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by Josue on 26/07/2016.
 */
@ApplicationScoped
public class FairJobStore {

    private static final Logger logger = Logger.getLogger(FairJobStore.class.getName());

    private static final String JOBS_MAP_PREFIX = "JOBS_";
    private static final String BACKUP_MAP_PREFIX = "BKP_";

    Queue<JobEvent> jobs;
    Map<String, JobEvent> backup;

    @Inject
    private PipelineStore executor;

    @Inject
    private HazelcastInstance hazelcast;

    @Resource
    private ManagedScheduledExecutorService mses;

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object arg) {
        String uuid = hazelcast.getCluster().getLocalMember().getUuid();

        jobs = hazelcast.getQueue(JOBS_MAP_PREFIX + uuid);
        backup = hazelcast.getMap(BACKUP_MAP_PREFIX + uuid);

        mses.scheduleAtFixedRate(() -> {
            JobEvent event;
            while ((event = get()) != null) {
                executor.getExecutor(event.getMasterId()).submit(event.wrapProperties());
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    @PreDestroy
    public void dispose() {
        mses.shutdown();
    }

    public JobEvent get() {
        JobEvent jobEvent = jobs.poll();
        if (jobEvent != null) {
            backup.put(jobEvent.getId(), jobEvent);
        }
        return jobEvent;
    }

    public void add(List<JobEvent> jobEvents) {
        Set<Member> members = hazelcast.getCluster().getMembers();

        int partitionSize = jobEvents.size() / members.size();

        logger.info(">>>>>  PARTITION SIZE: " + partitionSize);

        Queue<List<JobEvent>> splitted = splitList(jobEvents, partitionSize);
        for (Member member : members) {
            logger.info(">>>>>  ADDING TO MEMBER QUEUE: " + member.getUuid());
            IQueue<JobEvent> distQueue = hazelcast.getQueue(JOBS_MAP_PREFIX + member.getUuid());
            distQueue.addAll(splitted.poll());
        }
    }

    public JobEvent releaseJob(String id) {
        return backup.remove(id);
    }

    private <T> Queue<List<T>> splitList(List<T> originalList, final int partitionSize) {
        Queue<List<T>> partitions = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < originalList.size(); i += partitionSize) {
            partitions.add(originalList.subList(i, Math.min(i + partitionSize, originalList.size())));
        }
        return partitions;
    }
}
