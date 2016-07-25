package com.josue.batch.agent.chunklet;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.batch.agent.core.ChunkListener;
import com.josue.batch.agent.core.InstanceProvider;
import com.josue.batch.agent.core.SimpleInstanceProvider;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Josue on 28/04/2016.
 */
public class ChunkletExecutor extends ChunkExecutor {

    private final Class<? extends Chunklet> chunkletType;

    private static final Logger logger = Logger.getLogger(ChunkletExecutor.class.getName());

    public ChunkletExecutor(Class<? extends Chunklet> chunkletType,
                            ExecutorService service,
                            List<Class<ChunkListener>> listeners) {
        super(service, new SimpleInstanceProvider(), listeners);
        this.chunkletType = chunkletType;
    }

    public ChunkletExecutor(Class<? extends Chunklet> chunkletType,
                            List<Class<ChunkListener>> listeners,
                            ExecutorService service,
                            InstanceProvider provider) {
        super(service, provider, listeners);
        this.chunkletType = chunkletType;
    }

    @Override
    public void execute(Properties properties) throws Exception {
        final Properties props = new Properties();
        props.putAll(properties);

        String id = UUID.randomUUID().toString().substring(0, 8);

        logger.log(Level.FINE, "Starting job, id: {0}, properties: {1}", new Object[]{id, properties});

        logger.log(Level.FINER, "{0} - Initialising {0}", new Object[]{id, chunkletType.getName()});
        Chunklet chunklet = chunkletType.newInstance();
        chunklet.init(props);

        long start = System.currentTimeMillis();
        chunklet.execute();

        logger.log(Level.FINER, "{0} - Finished in {0}ms", new Object[]{id, System.currentTimeMillis() - start});
        chunklet.close();
    }
}
