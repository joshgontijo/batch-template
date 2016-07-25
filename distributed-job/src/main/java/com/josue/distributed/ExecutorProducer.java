package com.josue.distributed;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.batch.agent.core.ChunkListener;
import com.josue.batch.agent.stage.StageChunkExecutor;
import com.josue.distributed.job.SampleListener;
import com.josue.distributed.job.SampleProcessor;
import com.josue.distributed.job.SampleReader;
import com.josue.distributed.job.SampleWriter;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.LinkedList;
import java.util.List;
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

    @Produces
    public ChunkExecutor produces() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10,
                10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.AbortPolicy());


        List<Class<? extends ChunkListener>> listeners = new LinkedList<>();
        listeners.add(SampleListener.class);
        StageChunkExecutor stageChunkExecutor = new StageChunkExecutor(
                SampleReader.class,
                SampleProcessor.class,
                SampleWriter.class,
                listeners,
                threadPoolExecutor,
                new CDIInstanceProvider());

        return stageChunkExecutor;


    }
}
