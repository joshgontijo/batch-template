package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.Chunk;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class StageChunkProcessor extends Chunk {

    public abstract Object proccess(Object input) throws Exception;

}
