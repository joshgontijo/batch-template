package com.josue.batch.agent.core;

import java.io.Serializable;

/**
 * @author Josue Gontijo .
 */
public abstract class ChunkListener extends Chunk implements Serializable {

    public abstract void onStart();

    public abstract void onSuccess();

    public abstract void onFail(Exception ex);

}
