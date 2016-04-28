package com.josue.batch.agent.core;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;

/**
 * @author Josue Gontijo <josue.gontijo@maersk.com>.
 */
public abstract class Executor {

    private final List<Class<? extends ChunkListener>> listenersDef = new LinkedList<>();
    private final ExecutorService service;

    protected Executor(ExecutorService service, List<? extends Class<? extends ChunkListener>> listeners) {
        this.service = service;
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
                        ChunkListener chunkListener = listener.newInstance();
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
                    ex.printStackTrace();
                }
            }
        });
    }

}
