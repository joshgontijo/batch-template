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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Josue
 */
public class StageChunkExecutor extends ChunkExecutor {

    private static final Logger logger = Logger.getLogger(StageChunkExecutor.class.getName());

    private final Class<? extends StageChunkReader> readerType;
    private final Class<? extends StageChunkProcessor> processorType;
    private final Class<? extends StageChunkWriter> writerType;

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
        String id = UUID.randomUUID().toString().substring(0, 8);
        long jobStart = System.currentTimeMillis();

        logger.log(Level.FINE, "Starting job, id: {0}, properties: {1}", new Object[]{id, properties});

        logger.log(Level.FINER, "{0} - Initialising {0}", new Object[]{id, readerType.getName()});
        StageChunkReader reader = provider.newInstance(readerType);
        reader.init(properties);

        StageChunkProcessor processor = null;
        if (processorType != null) {
            logger.log(Level.FINER, "{0} - Initialising {0}", new Object[]{id, processorType.getName()});
            processor = provider.newInstance(processorType);
            processor.init(properties);
        }


        logger.log(Level.FINER, "{0} - Initialising {0}", new Object[]{id, writerType.getName()});
        StageChunkWriter writer = provider.newInstance(writerType);
        writer.init(properties);

        List processedItems = new LinkedList<>();
        Object item;
        while ((item = readWithLog(id, reader)) != null) {
            if (processor != null) {
                long procStart = System.currentTimeMillis();
                item = processor.proccess(item);
                logger.log(Level.FINER, "{0} - Processed in {1}ms", new Object[]{id, (System.currentTimeMillis() - procStart)});
            }
            processedItems.add(item);
        }

        long writeStart = System.currentTimeMillis();
        logger.log(Level.FINE, "{0} - Writting {1} items", new Object[]{id, (processedItems.size())});
        writer.write(processedItems);
        logger.log(Level.FINE, "{0} - Writed in {1}ms", new Object[]{id, (System.currentTimeMillis() - writeStart)});


        long closeStart = System.currentTimeMillis();
        logger.log(Level.FINER, "{0} - Closing...", new Object[]{id});
        reader.close();
        processor.close();
        writer.close();
        logger.log(Level.FINER, "{0} - Closed in {1}", new Object[]{id, (System.currentTimeMillis() - closeStart)});

        logger.log(Level.FINE, "{0} - Finished in {1}ms", new Object[]{id, (System.currentTimeMillis() - jobStart)});
    }

    private Object readWithLog(String id, StageChunkReader reader) throws Exception {
        long readStart = System.currentTimeMillis();
        Object read = reader.read();
        logger.log(Level.FINER, "{0} - Read {1}ms", new Object[]{id, (System.currentTimeMillis() - readStart)});
        return read;
    }

}
