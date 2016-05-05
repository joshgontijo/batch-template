package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.batch.agent.core.ChunkListener;
import com.josue.batch.agent.core.InstanceProvider;
import com.josue.batch.agent.core.SimpleInstanceProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

/**
 * Created by Josue
 */
public class StageChunkExecutor extends ChunkExecutor {

    private final Class<? extends StageChunkReader> readerType;
    private final Class<? extends StageChunkProcessor> processorType;
    private final Class<? extends StageChunkWriter> writerType;

    private boolean debug = true;

    private static final Logger logger = Logger.getLogger(StageChunkExecutor.class.getName());

    public StageChunkExecutor(Class<? extends StageChunkReader> readerType,
                              Class<? extends StageChunkWriter> writerType,
                              List<Class<? extends ChunkListener>> listeners,
                              ExecutorService service) {
        this(readerType, null, writerType, listeners, service, new SimpleInstanceProvider());
    }

    public StageChunkExecutor(Class<? extends StageChunkReader> readerType,
                              Class<? extends StageChunkWriter> writerType,
                              List<Class<? extends ChunkListener>> listeners,
                              ExecutorService service,
                              InstanceProvider provider) {
        this(readerType, null, writerType, listeners, service, provider);
    }

    public StageChunkExecutor(Class<? extends StageChunkReader> readerType,
                              Class<? extends StageChunkProcessor> processorType,
                              Class<? extends StageChunkWriter> writerType,
                              List<Class<? extends ChunkListener>> listeners,
                              ExecutorService service,
                              InstanceProvider provider) {
        super(service, provider, listeners);
        if (readerType == null || writerType == null) {
            throw new IllegalArgumentException("Reader and writer must be specified");
        }
        this.readerType = readerType;
        this.processorType = processorType;
        this.writerType = writerType;
    }


    @Override
    public void execute(Properties properties) throws Exception {
        String id = UUID.randomUUID().toString().substring(0, 4);
        StageChunkReader reader = provider.newInstance(readerType);
        reader.init(properties);

        StageChunkProcessor processor = null;
        if (processorType != null) {
            processor = provider.newInstance(processorType);
            processor.init(properties);
        }

        StageChunkWriter writer = provider.newInstance(writerType);
        writer.init(properties);

        List processedItems = new LinkedList<>();
        Object item;
        while ((item = readWithLog(id, reader)) != null) {
            if (processor != null) {
                long procStart = System.currentTimeMillis();
                item = processor.proccess(item);
                if (debug) {
                    logger.info("(" + id + ") Process " + (System.currentTimeMillis() - procStart) + "ms");
                }
            }
            processedItems.add(item);
        }

        long writeStart = System.currentTimeMillis();
        writer.write(processedItems);
        if (debug) {
            logger.info("(" + id + ") Write " + (System.currentTimeMillis() - writeStart) + "ms");
        }

        long readerClose = System.currentTimeMillis();
        reader.close();
        if (debug) {
            logger.info("(" + id + ") Reader.close " + (System.currentTimeMillis() - readerClose) + "ms");
        }
        long procClose = System.currentTimeMillis();
        processor.close();
        if (debug) {
            logger.info("(" + id + ") Processor.close " + (System.currentTimeMillis() - procClose) + "ms");
        }
        long writeClose = System.currentTimeMillis();
        writer.close();
        if (debug) {
            logger.info("(" + id + ") Writer.close " + (System.currentTimeMillis() - writeClose) + "ms");
        }


    }

    private Object readWithLog(String id, StageChunkReader reader) throws Exception {
        long readStart = System.currentTimeMillis();
        Object read = reader.read();
        if (debug) {
            logger.info("(" + id + ") Read " + (System.currentTimeMillis() - readStart) + "ms");
        }
        return read;
    }

    public void debug(boolean debug) {
        this.debug = debug;
    }
}
