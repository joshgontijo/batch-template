package com.josue.distributed;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.batch.agent.stage.StageChunkConfig;
import com.josue.batch.agent.stage.StageChunkExecutor;
import com.josue.distributed.job.SampleListener;
import com.josue.distributed.job.SampleProcessor;
import com.josue.distributed.job.SampleReader;
import com.josue.distributed.job.SampleWriter;

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
        StageChunkConfig config = new StageChunkConfig(SampleReader.class, SampleProcessor.class, SampleWriter.class)
                .addListener(SampleListener.class)
                .instanceProvider(new CDIInstanceProvider())
                .executorThreadFactory(threadFactory);


        chunkExecutor = new StageChunkExecutor(config);
    }

    @Produces
    public ChunkExecutor produces() {
        return chunkExecutor;
    }
}
