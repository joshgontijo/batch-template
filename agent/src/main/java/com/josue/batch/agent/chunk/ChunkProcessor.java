package com.josue.batch.agent.chunk;

import java.io.Serializable;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class ChunkProcessor<T> extends Chunk implements Serializable  {

    public abstract T proccess(T input);

}
