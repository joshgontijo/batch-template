package com.josue.batch.agent.core;

import java.io.Serializable;

/**
 * @author Josue Gontijo
 */
public abstract class ChunkListener extends ChunkTemplate implements Serializable {

    public abstract void onStart() throws Exception;

    public abstract void onSuccess() throws Exception;

    public abstract void onFail(Exception ex);

}
