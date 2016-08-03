package com.josue.distributed;

import com.josue.distributed.config.PipelineStore;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.util.logging.Logger;

/**
 * Created by Josue on 02/08/2016.
 */
@ClientEndpoint(encoders = ChunkDecoder.class, decoders = ChunkDecoder.class)
public class ChunkWSClient {

    private static final Logger logger = Logger.getLogger(ChunkWSClient.class.getName());

    private Session currentSession = null;

    private PipelineStore store;

    public ChunkWSClient(PipelineStore store) {
        this.store = store;
    }

    @OnOpen
    public void onOpen(final Session userSession) {
        logger.info(":: Connection openned ::");
        this.currentSession = userSession;
    }

    @OnClose
    public void onClose(final Session userSession, final CloseReason reason) {
        this.currentSession = null;
    }

    @OnMessage
    public void onMessage(final ChunkEvent event) {
        store.getExecutor(event.getMasterId()).submit(event.wrapProperties());
    }

}
