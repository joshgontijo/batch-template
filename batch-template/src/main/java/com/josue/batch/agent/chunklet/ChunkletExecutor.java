package com.josue.batch.agent.chunklet;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.batch.agent.metric.MeterHint;

import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by Josue on 28/04/2016.
 */
public class ChunkletExecutor extends ChunkExecutor {

    private final ChunkletConfig config;

    public ChunkletExecutor(ChunkletConfig config) {
        super(config);
        if (config.getChunklet() == null) {
            throw new IllegalStateException("Chunklet type cannot be null");
        }
        this.config = config;
    }

    @Override
    protected void execute(String id, Properties properties) throws Exception {

        logger.log(Level.FINER, "{0} - Initialising {0}", new Object[]{id, config.getChunklet().getName()});
        Chunklet chunklet = config.getChunklet().newInstance();
        
        meter.start(MeterHint.CHUNKLETINIT);
        chunklet.init(properties, meter);
        meter.end(MeterHint.CHUNKLETINIT);

        meter.start(MeterHint.CHUNKLETEXECUTE);
        chunklet.execute();
        meter.end(MeterHint.CHUNKLETEXECUTE);

        meter.start(MeterHint.CHUNKLETCLOSE);
        chunklet.close();
        meter.end(MeterHint.CHUNKLETCLOSE);
    }
}
