package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.Chunk;

import java.util.List;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class StageChunkWriter<T> extends Chunk {

    public abstract void write(List<T> items) throws Exception;
}
