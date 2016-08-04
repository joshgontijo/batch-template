package com.josue.batch.agent.sample;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.batch.agent.sample.fixture.SampleListener;
import com.josue.batch.agent.sample.fixture.SampleReader;
import com.josue.batch.agent.sample.fixture.SampleWriter;
import com.josue.batch.agent.stage.StageChunkConfig;
import com.josue.batch.agent.stage.StageChunkExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Created by Josue on 27/04/2016.
 */
@ApplicationScoped
public class ExecutorProducer {

    @Resource
    ManagedThreadFactory threadFactory;

    private ChunkExecutor chunkExecutor;

    @PostConstruct
    public void init() {

        StageChunkConfig config = new StageChunkConfig(SampleReader.class, SampleWriter.class);
        config.getListeners().add(SampleListener.class);
        config.instanceProvider(new CDIInstanceProvider());
        config.executorThreadFactory(threadFactory);

        chunkExecutor = new StageChunkExecutor(config);
    }

    @Produces
    public ChunkExecutor produces() {
        return chunkExecutor;
    }

}
