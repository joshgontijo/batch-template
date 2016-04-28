package com.josue.batch.agent.chunklet;

import com.josue.batch.agent.core.PropertyAware;

/**
 * @author Josue Gontijo.
 */
public abstract class Chunklet extends PropertyAware {

    public abstract void execute();
}
