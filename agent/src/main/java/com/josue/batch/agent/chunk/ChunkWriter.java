package com.josue.batch.agent.chunk;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class ChunkWriter<T> extends Chunk implements Serializable {

    protected abstract void write(List<T> items);
}
