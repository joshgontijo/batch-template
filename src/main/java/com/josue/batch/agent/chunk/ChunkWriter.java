package com.josue.batch.agent.chunk;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class ChunkWriter<T> extends PropertyAware implements Serializable {

    public abstract void write(List<T> items);
}
