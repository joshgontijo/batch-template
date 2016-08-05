package com.josue.batch.agent.core;

import com.josue.batch.agent.metric.Meter;
import com.josue.batch.agent.metric.MeterHint;
import com.josue.batch.agent.metric.Metric;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Josue Gontijo
 */
public abstract class ChunkExecutor {

    private static final Object LOCK = new Object();
    private final CoreConfiguration config;
    private boolean shutdownRequest = false;

    //shared with child classes
    protected Meter meter;
    protected Logger logger;
    protected InstanceProvider instanceProvider;

    protected ChunkExecutor(final CoreConfiguration config) {
        this.config = config;
        this.meter = config.getMeter();
        this.logger = config.getLogger();
        this.instanceProvider = config.getInstanceProvider();
    }

    protected abstract void execute(String id, Properties properties) throws Exception;

    public Metric getMetric() {
        return new Metric(config.getExecutor(), meter);
    }

    public void shutdown() {
        synchronized (LOCK) {
            if (!shutdownRequest) {
                logger.info("Shutdown request, no more tasks will be accepted");
                shutdownRequest = true;
                config.getExecutor().shutdown();
            }
        }
    }

    public boolean shutdown(long timeout, TimeUnit timeUnit) throws InterruptedException {
        synchronized (LOCK) {
            if (!shutdownRequest) {
                logger.info("Shutdown request, no more tasks will be accepted");
                shutdownRequest = true;
                config.getExecutor().shutdown();
            }
            return config.getExecutor().awaitTermination(timeout, timeUnit);
        }
    }

    public void submit(Properties properties) {
        if (shutdownRequest) {
            throw new RuntimeException("Executor is closed");
        }
        //copy properties
        final Properties props = new Properties();
        props.putAll(properties);

        final String id = UUID.randomUUID().toString().substring(0, 8);
        final long jobStart = System.currentTimeMillis();

        final List<Class<? extends ChunkListener>> listenersTypes = config.getListeners();
        final InstanceProvider instanceProvider = config.getInstanceProvider();

        config.getExecutor().submit(() -> {

            meter.start(MeterHint.TOTAL);
            logger.log(Level.FINER, "Starting job, id: {0}, properties: {1}", new Object[]{id, props});

            List<ChunkListener> listeners = new LinkedList<>();
            try {
                for (Class<? extends ChunkListener> listener : listenersTypes) {
                    ChunkListener chunkListener = instanceProvider.newInstance(listener);
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
                //------ ONERROR ------
                logger.log(Level.SEVERE, ex.getMessage(), ex);
                meter.start(MeterHint.ONERROR);
                for (ChunkListener listener : listeners) {
                    try {
                        listener.onFail(ex);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
                meter.end(MeterHint.ONERROR);
            }
            logger.log(Level.FINER, "{0} - Finished in {1}ms", new Object[]{id, (System.currentTimeMillis() - jobStart)});
            meter.end(MeterHint.TOTAL);
        });
    }
}
