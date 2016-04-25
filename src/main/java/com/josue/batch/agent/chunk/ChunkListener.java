package com.josue.batch.agent.chunk;

import java.io.Serializable;

/**
 * @author Josue Gontijo .
 */
public abstract class ChunkListener extends PropertyAware implements Serializable {

    public abstract void onStart();

    public abstract void onSuccess();

    public abstract void onFail(Exception ex);

}
