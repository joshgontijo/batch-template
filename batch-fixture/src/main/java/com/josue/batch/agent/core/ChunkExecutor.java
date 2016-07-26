package com.josue.batch.agent.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Josue Gontijo
 */
public abstract class ChunkExecutor {

    protected static final Logger logger = Logger.getLogger(ChunkExecutor.class.getName());
    protected final InstanceProvider provider;
    private final List<Class<? extends ChunkListener>> listenersDef = new LinkedList<>();
    private final ExecutorService service;

    protected ChunkExecutor(CoreConfiguration config) {
        this.service = config.getExecutorService();
        this.provider = config.getInstanceProvider();
        for (Class<? extends ChunkListener> l : config.getListeners()) {
            listenersDef.add(l);
        }

        logger.setLevel(config.getLogLevel());
    }

    protected abstract void execute(String id, Properties properties) throws Exception;

    public void submit(Properties properties) {
        //copy properties
        final Properties props = new Properties();
        props.putAll(properties);

        final String id = UUID.randomUUID().toString().substring(0, 8);
        final long jobStart = System.currentTimeMillis();

        service.submit(new Runnable() {
            @Override
            public void run() {
                logger.log(Level.FINE, "Starting job, id: {0}, properties: {1}", new Object[]{id, props});

                List<ChunkListener> listeners = new LinkedList<>();
                try {
                    for (Class<? extends ChunkListener> listener : listenersDef) {
                        ChunkListener chunkListener = provider.newInstance(listener);
                        chunkListener.init(props);
                        listeners.add(chunkListener);
                    }

                    //onstart
                    for (ChunkListener listener : listeners) {
                        listener.onStart();
                    }

                    execute(id, props);

                    //on sucess
                    for (ChunkListener listener : listeners) {
                        listener.onSuccess();
                    }

                } catch (Exception ex) {
                    //on error
                    ex.printStackTrace();
                    for (ChunkListener listener : listeners) {
                        try {
                            listener.onFail(ex);
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, ex.getMessage(), ex);
                        }
                    }
                    logger.log(Level.SEVERE, ex.getMessage(), ex);
                }
                logger.log(Level.FINE, "{0} - Finished in {1}ms", new Object[]{id, (System.currentTimeMillis() - jobStart)});
            }
        });
    }
}
