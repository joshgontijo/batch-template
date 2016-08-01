package com.josue.batch.agent.chunklet;

import com.josue.batch.agent.core.ChunkExecutor;

import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by Josue on 28/04/2016.
 */
public class ChunkletExecutor extends ChunkExecutor {

    private final Class<? extends Chunklet> chunkletType;

    public ChunkletExecutor(ChunkletConfig config) {
        super(config);
        if (config.getChunklet() == null) {
            throw new IllegalStateException("Chunklet type cannot be null");
        }
        this.chunkletType = config.getChunklet();
    }

    @Override
    protected void execute(String id, Properties properties) throws Exception {

        logger.log(Level.FINER, "{0} - Initialising {0}", new Object[]{id, chunkletType.getName()});
        Chunklet chunklet = chunkletType.newInstance();
        chunklet.init(properties);

        chunklet.execute();
        chunklet.close();
    }
}
