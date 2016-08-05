package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.ChunkTemplate;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class StageChunkReader extends ChunkTemplate {

    public abstract Object read() throws Exception;
}
