package com.josue.batch.agent.chunklet;

import com.josue.batch.agent.core.ChunkTemplate;

/**
 * @author Josue Gontijo.
 */
public abstract class Chunklet extends ChunkTemplate {

    public abstract void execute() throws Exception;
}
