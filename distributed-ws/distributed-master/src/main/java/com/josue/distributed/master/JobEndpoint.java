package com.josue.distributed.master;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Josue on 02/08/2016.
 */
@ApplicationScoped
@ServerEndpoint(value = "/jobs",
        encoders = ChunkDecoder.class,
        decoders = ChunkDecoder.class)
public class JobEndpoint {

    private static final Object LOCK = new Object();

    private static final Logger logger = Logger.getLogger(JobEndpoint.class.getName());
    private static final List<Session> sessions = new LinkedList<>();
    private static Iterator<Session> iterator = null;

    @OnOpen
    public void onOpen(Session session) {
        logger.log(Level.INFO, "Connected ... {0}", session.getId());
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(ChunkEvent event, Session session) {
//        logger.info(":: JOB FINISHED " + event);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }

    public void startChunk(List<ChunkEvent> chunks) {
        if (sessions.isEmpty()) {
            logger.warning(":: No worker connected ::");
            return;
        }
        synchronized (LOCK) {
            if (iterator == null || !iterator.hasNext()) {
                iterator = sessions.iterator();
            }
            Session session = iterator.next();
            session.getAsyncRemote().sendObject(chunks);
        }

    }
}