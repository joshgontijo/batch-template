package com.josue.batch.agent.chunk;

import java.io.Serializable;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class ChunkReader<T> extends Chunk implements Serializable {

    protected abstract T read();
}
