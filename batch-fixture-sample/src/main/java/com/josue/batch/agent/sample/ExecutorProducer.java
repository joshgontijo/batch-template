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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10,
                10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(100),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());

        StageChunkConfig config = new StageChunkConfig(SampleReader.class, SampleWriter.class)
                .addListener(SampleListener.class)
                .instanceProvider(new CDIInstanceProvider())
                .executor(executor);


        chunkExecutor = new StageChunkExecutor(config);
    }

    @Produces
    public ChunkExecutor produces() {
        return chunkExecutor;
    }

}
