package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.ChunkTemplate;

import java.util.List;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class StageChunkWriter extends ChunkTemplate {

    public abstract void write(List<Object> items) throws Exception;
}
