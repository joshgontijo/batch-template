package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.Chunk;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class StageChunkProcessor<T> extends Chunk {

    public abstract T proccess(T input) throws Exception;

}
