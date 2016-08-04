package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.ChunkExecutor;
import com.josue.batch.agent.metric.MeterHint;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by Josue
 */
public class StageChunkExecutor extends ChunkExecutor {

    private final Class<? extends StageChunkReader> readerType;
    private final Class<? extends StageChunkProcessor> processorType;
    private final Class<? extends StageChunkWriter> writerType;

    public StageChunkExecutor(final StageChunkConfig config) {
        super(config);
        if (config.getReader() == null || config.getWriter() == null) {
            throw new IllegalArgumentException("Reader and writer must be specified");
        }
        this.readerType = config.getReader();
        this.processorType = config.getProcessor();
        this.writerType = config.getWriter();
    }


    @Override
    protected void execute(String id, Properties properties) throws Exception {
        logger.log(Level.FINER, "{0} - Initialising {0}", new Object[]{id, readerType.getName()});
        StageChunkReader reader = provider.newInstance(readerType);
        meter.start(MeterHint.READERINIT);
        reader.init(properties, meter);
        meter.end(MeterHint.READERINIT);

        StageChunkProcessor processor = null;
        if (processorType != null) {
            logger.log(Level.FINER, "{0} - Initialising {0}", new Object[]{id, processorType.getName()});
            processor = provider.newInstance(processorType);

            meter.start(MeterHint.PROCESSORINIT);
            processor.init(properties, meter);
            meter.end(MeterHint.PROCESSORINIT);
        }


        logger.log(Level.FINER, "{0} - Initialising {0}", new Object[]{id, writerType.getName()});
        StageChunkWriter writer = provider.newInstance(writerType);
        meter.start(MeterHint.WRITERINIT);
        writer.init(properties, meter);
        meter.end(MeterHint.WRITERINIT);

        List processedItems = new LinkedList<>();
        Object item;
        while ((item = readWithLog(id, reader)) != null) {
            if (processor != null) {
                long procStart = System.currentTimeMillis();

                meter.start(MeterHint.PROCCESS);
                item = processor.proccess(item);
                meter.end(MeterHint.PROCCESS);
                logger.log(Level.FINER, "{0} - Processed in {1}ms", new Object[]{id, (System.currentTimeMillis() - procStart)});
            }
            processedItems.add(item);
        }

        long writeStart = System.currentTimeMillis();
        logger.log(Level.FINER, "{0} - Writting {1} items", new Object[]{id, (processedItems.size())});
        meter.start(MeterHint.WRITE);
        writer.write(processedItems);
        meter.end(MeterHint.WRITE);
        logger.log(Level.FINER, "{0} - Writed in {1}ms", new Object[]{id, (System.currentTimeMillis() - writeStart)});


        long closeStart = System.currentTimeMillis();
        logger.log(Level.FINER, "{0} - Closing...", new Object[]{id});

        meter.start(MeterHint.READCLOSE);
        reader.close();
        meter.end(MeterHint.READCLOSE);

        if (processor != null) {
            meter.start(MeterHint.PROCCESSORCLOSE);
            processor.close();
            meter.end(MeterHint.PROCCESSORCLOSE);
        }
        meter.start(MeterHint.WRITECLOSE);
        writer.close();
        meter.end(MeterHint.WRITECLOSE);
        logger.log(Level.FINER, "{0} - Closed in {1}", new Object[]{id, (System.currentTimeMillis() - closeStart)});

    }

    private Object readWithLog(String id, StageChunkReader reader) throws Exception {
        long readStart = System.currentTimeMillis();

        meter.start(MeterHint.READ);
        Object read = reader.read();
        meter.end(MeterHint.READ);

        logger.log(Level.FINER, "{0} - Read {1}ms", new Object[]{id, (System.currentTimeMillis() - readStart)});
        return read;
    }

}
