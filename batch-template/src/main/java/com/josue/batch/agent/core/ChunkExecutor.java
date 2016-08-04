package com.josue.batch.agent.core;

import com.josue.batch.agent.metric.Meter;
import com.josue.batch.agent.metric.Metric;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Josue Gontijo
 */
public abstract class ChunkExecutor {

    public static final Object LOCK = new Object();

    protected static final Logger logger = Logger.getLogger(ChunkExecutor.class.getName());

    private final List<Class<? extends ChunkListener>> listenersDef = new LinkedList<>();

    protected final InstanceProvider provider;
    private final ThreadPoolExecutor executor;

    protected final Meter meter;

    private boolean shutdownRequest = false;

    protected ChunkExecutor(CoreConfiguration config) {
        this.executor = config.getExecutor();
        this.provider = config.getInstanceProvider();
        this.meter = new Meter(config.isEnableMetrics());
        for (Class<? extends ChunkListener> l : config.getListeners()) {
            listenersDef.add(l);
        }

        logger.setLevel(config.getLogLevel());
    }

    protected abstract void execute(String id, Properties properties) throws Exception;

    public Metric getMetric() {
        return new Metric(executor, meter);
    }

    public void shutdown() {
        synchronized (LOCK) {
            logger.info("Shutdown request, no more tasks will be accepted");
            shutdownRequest = true;
            executor.shutdown();
        }
    }

    public void awaitTermination(long timeout, TimeUnit timeUnit) throws InterruptedException {
        shutdownRequest = true;
        executor.awaitTermination(timeout, timeUnit);
    }

    public void submit(Properties properties) {
        synchronized (LOCK) {
            if (shutdownRequest) {
                throw new RuntimeException("Executor is closed");
            }
        }
        //copy properties
        final Properties props = new Properties();
        props.putAll(properties);

        final String id = UUID.randomUUID().toString().substring(0, 8);
        final long jobStart = System.currentTimeMillis();

        executor.submit(new Runnable() {
            @Override
            public void run() {
                meter.start(MeterHint.TOTAL);
                logger.log(Level.FINER, "Starting job, id: {0}, properties: {1}", new Object[]{id, props});

                List<ChunkListener> listeners = new LinkedList<>();
                try {
                    for (Class<? extends ChunkListener> listener : listenersDef) {
                        ChunkListener chunkListener = provider.newInstance(listener);
                        meter.start(MeterHint.LISTENERINIT);
                        chunkListener.init(props, meter);
                        meter.end(MeterHint.LISTENERINIT);
                        listeners.add(chunkListener);
                    }

                    //------ ONSTART ------
                    meter.start(MeterHint.ONSTART);
                    for (ChunkListener listener : listeners) {
                        listener.onStart();
                    }
                    meter.end(MeterHint.ONSTART);

                    //------ EXECUTE ------
                    execute(id, props);

                    //------ ONSUCCES ------
                    meter.start(MeterHint.ONSUCCESS);
                    for (ChunkListener listener : listeners) {
                        listener.onSuccess();
                    }
                    meter.end(MeterHint.ONSUCCESS);

                } catch (Exception ex) {
                    //on error
                    ex.printStackTrace();
                    meter.start(MeterHint.ONERROR);
                    for (ChunkListener listener : listeners) {
                        try {
                            listener.onFail(ex);
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                    meter.end(MeterHint.ONERROR);
                    logger.log(Level.SEVERE, ex.getMessage(), ex);
                }
                logger.log(Level.FINER, "{0} - Finished in {1}ms", new Object[]{id, (System.currentTimeMillis() - jobStart)});
                meter.end(MeterHint.TOTAL);
            }
        });
    }
}
