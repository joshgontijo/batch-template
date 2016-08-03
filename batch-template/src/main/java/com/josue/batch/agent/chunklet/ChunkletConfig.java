package com.josue.batch.agent.chunklet;

import com.josue.batch.agent.core.CoreConfiguration;

/**
 * Created by Josue on 25/07/2016.
 */
public class ChunkletConfig extends CoreConfiguration {

    private final Class<? extends Chunklet> chunkletType;

    public ChunkletConfig(Class<? extends Chunklet> chunklet) {
        this.chunkletType = chunklet;
    }

    Class<? extends Chunklet> getChunklet() {
        return chunkletType;
    }
}
