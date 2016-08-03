package com.josue.distributed;

import com.josue.distributed.config.PipelineStore;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Created by Josue on 02/08/2016.
 */
@ApplicationScoped
public class Bootstrap {

    private static final Logger logger = Logger.getLogger(Bootstrap.class.getName());

    private Session session;

    @Inject
    private PipelineStore store;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object arg) {
        LogManager.getLogManager().getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.mongodb.driver.management").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.mongodb.driver.cluster").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.mongodb.driver.protocol.insert").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.mongodb.driver.protocol.query").setLevel(Level.OFF);
        LogManager.getLogManager().getLogger("org.mongodb.driver.protocol.update").setLevel(Level.OFF);

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        ChunkWSClient endpoint = new ChunkWSClient(store);

        try {

            logger.info(":: WAITING FOR MASTER TO BE DEPLOYED -- REMOVE ME ::");
            Thread.sleep(15000);
            logger.info(":: CONNECTING TO MASTER -- REMOVE ME ::");

//            URI uri = new URI("ws://localhost:8080/distributed-master/jobs");
            URI uri = new URI("ws://master:8081/distributed-master/jobs");
            session = container.connectToServer(endpoint, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Produces
    private Session produces() {
        return session;
    }
}
