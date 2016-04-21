package com.josue.batch.agent.chunk;

import java.io.Serializable;

/**
 * @author Josue Gontijo <josue.gontijo@maersk.com>.
 */
public abstract class ChunkListener extends Chunk implements Serializable {

    protected abstract void onStart();

    protected abstract void onSuccess();

    protected abstract void onFail(Exception ex);

}
