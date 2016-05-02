package com.josue.batch.agent.chunklet;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.batch.agent.core.ChunkListener;
import com.josue.batch.agent.core.InstanceProvider;
import com.josue.batch.agent.core.SimpleInstanceProvider;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * Created by Josue on 28/04/2016.
 */
public class ChunkletExecutor extends ChunkExecutor {

    private final Class<? extends Chunklet> chunkletType;

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
        Chunklet chunklet = chunkletType.newInstance();
        chunklet.init(properties);
        chunklet.execute();
        chunklet.close();
    }
}
