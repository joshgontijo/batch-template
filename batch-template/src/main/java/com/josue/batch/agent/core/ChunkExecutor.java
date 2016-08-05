package com.josue.batch.agent.core;

import com.josue.batch.agent.metric.MeterHint;
import com.josue.batch.agent.metric.Metric;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @author Josue Gontijo
 */
public abstract class ChunkExecutor {

    public static final Object LOCK = new Object();
    private final CoreConfiguration config;
    private boolean shutdownRequest = false;

    protected ChunkExecutor(final CoreConfiguration config) {
        this.config = config;
    }

    protected abstract void execute(String id, Properties properties) throws Exception;

    public Metric getMetric() {
        return new Metric(config.getExecutor(), config.getMeter());
    }

    public void shutdown() {
        synchronized (LOCK) {
            config.getLogger().info("Shutdown request, no more tasks will be accepted");
            shutdownRequest = true;
            config.getExecutor().shutdown();
        }
    }

    public boolean shutdown(long timeout, TimeUnit timeUnit) throws InterruptedException {
        shutdown();
        return config.getExecutor().awaitTermination(timeout, timeUnit);
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

        config.getExecutor().submit((Runnable) () -> {
            config.getMeter().start(MeterHint.TOTAL);
            config.getLogger().log(Level.FINER, "Starting job, id: {0}, properties: {1}", new Object[]{id, props});

            List<ChunkListener> listeners = new LinkedList<>();
            try {
                for (Class<? extends ChunkListener> listener : config.getListeners()) {
                    ChunkListener chunkListener = config.getInstanceProvider().newInstance(listener);
                    config.getMeter().start(MeterHint.LISTENERINIT);
                    chunkListener.init(props, config.getMeter());
                    config.getMeter().end(MeterHint.LISTENERINIT);
                    listeners.add(chunkListener);
                }

                //------ ONSTART ------
                config.getMeter().start(MeterHint.ONSTART);
                for (ChunkListener listener : listeners) {
                    listener.onStart();
                }
                config.getMeter().end(MeterHint.ONSTART);

                //------ EXECUTE ------
                execute(id, props);

                //------ ONSUCCES ------
                config.getMeter().start(MeterHint.ONSUCCESS);
                for (ChunkListener listener : listeners) {
                    listener.onSuccess();
                }
                config.getMeter().end(MeterHint.ONSUCCESS);

            } catch (Exception ex) {
                //------ ONERROR ------
                config.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
                config.getMeter().start(MeterHint.ONERROR);
                for (ChunkListener listener : listeners) {
                    try {
                        listener.onFail(ex);
                    } catch (Exception e) {
                        config.getLogger().log(Level.SEVERE, ex.getMessage(), ex);
                    }
                }
                config.getMeter().end(MeterHint.ONERROR);
            }
            config.getLogger().log(Level.FINER, "{0} - Finished in {1}ms", new Object[]{id, (System.currentTimeMillis() - jobStart)});
            config.getMeter().end(MeterHint.TOTAL);
        });
    }
}
