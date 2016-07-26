package com.josue.batch.agent.chunklet;

import com.josue.batch.agent.core.ChunkListener;
import com.josue.batch.agent.core.CoreConfiguration;
import com.josue.batch.agent.core.InstanceProvider;

import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

/**
 * Created by Josue on 25/07/2016.
 */
public class ChunkletConfig extends CoreConfiguration {

    private final Class<? extends Chunklet> chunkletType;

    public ChunkletConfig(Class<? extends Chunklet> chunklet) {
        this.chunkletType = chunklet;
    }

    @Override
    public ChunkletConfig executor(ExecutorService executorService) {
        super.executor(executorService);
        return this;
    }

    @Override
    public ChunkletConfig instanceProvider(InstanceProvider provider) {
        super.instanceProvider(provider);
        return this;
    }

    @Override
    public ChunkletConfig addListener(Class<? extends ChunkListener> listener) {
        super.addListener(listener);
        return this;
    }

    @Override
    public ChunkletConfig logLevel(Level level) {
        super.logLevel(level);
        return this;
    }

    Class<? extends Chunklet> getChunklet() {
        return chunkletType;
    }
}
