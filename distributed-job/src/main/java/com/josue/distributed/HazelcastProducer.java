package com.josue.distributed;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

/**
 * Created by Josue on 27/07/2016.
 */
@ApplicationScoped
public class HazelcastProducer {

    private HazelcastInstance hazelcast;

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object arg) {
        String mongo = System.getenv("MONGO_PORT_27017_TCP_ADDR");
        System.out.println("--->> " + mongo);
        hazelcast = Hazelcast.newHazelcastInstance();
    }

    @Produces
    public HazelcastInstance produce() {
        return hazelcast;
    }

    @PreDestroy
    public void dispose() {
        hazelcast.shutdown();
    }
}
