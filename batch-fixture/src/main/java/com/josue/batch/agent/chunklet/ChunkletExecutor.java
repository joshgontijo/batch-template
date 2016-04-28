package com.josue.batch.agent.chunklet;

import com.josue.batch.agent.core.ChunkListener;
import com.josue.batch.agent.core.Executor;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * Created by Josue on 28/04/2016.
 */
public class ChunkletExecutor extends Executor {

    private final Class<? extends Chunklet> chunkletType;

    public ChunkletExecutor(Class<? extends Chunklet> chunkletType,
                            ExecutorService service,
                            List<Class<ChunkListener>> listeners) {
        super(service, listeners);
        this.chunkletType = chunkletType;
    }

    @Override
    public void execute(Properties properties) throws Exception {
        Chunklet chunklet = chunkletType.newInstance();
        chunklet.init(properties);
        chunklet.execute();
    }
}
