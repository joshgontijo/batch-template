package com.josue.batch.agent.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Josue Gontijo <josue.gontijo@maersk.com>.
 */
public abstract class ChunkExecutor {

    private static final Logger logger = Logger.getLogger(ChunkExecutor.class.getName());

    private final List<Class<? extends ChunkListener>> listenersDef = new LinkedList<>();
    private final ExecutorService service;
    protected final InstanceProvider provider;

    protected ChunkExecutor(ExecutorService service, InstanceProvider provider, List<? extends Class<? extends ChunkListener>> listeners) {
        this.service = service;
        this.provider = provider;
        for (Class<? extends ChunkListener> l : listeners) {
            listenersDef.add(l);
        }
    }

    public abstract void execute(Properties properties) throws Exception;

    public void submit(final Properties properties) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                List<ChunkListener> listeners = new LinkedList<>();
                try {
                    for (Class<? extends ChunkListener> listener : listenersDef) {
                        ChunkListener chunkListener = provider.newInstance(listener);
                        chunkListener.init(properties);
                        listeners.add(chunkListener);
                    }

                    //onstart
                    for (ChunkListener listener : listeners) {
                        listener.onStart();
                    }

                    execute(properties);

                    //on sucess
                    for (ChunkListener listener : listeners) {
                        listener.onSuccess();
                    }

                } catch (Exception ex) {
                    //on error
                    ex.printStackTrace();
                    for (ChunkListener listener : listeners) {
                        listener.onFail(ex);
                    }
                    logger.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        });
    }

}
