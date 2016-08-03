package com.josue.distributed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Josue on 18/06/2016.
 */
public class ChunkDecoder implements Decoder.Text<ChunkEvent>, Encoder.Text<ChunkEvent> {

    private static final Logger logger = Logger.getLogger(ChunkEvent.class.getName());

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String encode(ChunkEvent event) throws EncodeException {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Could not encode event", e);
            throw new EncodeException(event, "Could not encode event", e);
        }
    }

    @Override
    public ChunkEvent decode(String s) throws DecodeException {
        try {
            return mapper.readValue(s, ChunkEvent.class);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not decode event", e);
            throw new DecodeException(s, "Could not encode event", e);
        }
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {

    }
}
