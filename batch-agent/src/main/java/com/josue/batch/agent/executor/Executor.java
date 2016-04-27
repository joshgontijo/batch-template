package com.josue.batch.agent.executor;

import com.josue.batch.agent.core.ChunkListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author Josue Gontijo <josue.gontijo@maersk.com>.
 */
public abstract class Executor implements Runnable {

    private Properties properties;
    private List<ChunkListener> listeners = new LinkedList<>();
    private List<Class<ChunkListener>> listenersDef = new LinkedList<>();

    public Executor(List<Class<ChunkListener>> listeners) {
        this.listenersDef = listeners;
    }

    public abstract void execute() throws Exception;

    public abstract void init(Properties properties) throws Exception;

    @Override
    public void run() {
        try {
            init(properties);
            this.init();

            //onstart
            for (ChunkListener listener : listeners) {
                listener.onStart();
            }

            execute();

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

    private final void init() throws Exception {
        for (Class<? extends ChunkListener> listener : listenersDef) {
            ChunkListener chunkListener = listener.newInstance();
            listeners.add(chunkListener);
        }
    }

}
