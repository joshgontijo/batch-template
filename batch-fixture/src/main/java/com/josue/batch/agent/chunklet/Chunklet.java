package com.josue.batch.agent.chunklet;

import com.josue.batch.agent.core.Chunk;

/**
 * @author Josue Gontijo.
 */
public abstract class Chunklet extends Chunk {

    public abstract void execute() throws Exception;
}
