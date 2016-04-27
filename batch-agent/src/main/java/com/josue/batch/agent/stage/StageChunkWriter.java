package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.PropertyAware;

import java.util.List;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class StageChunkWriter<T> extends PropertyAware {

    public abstract void write(List<T> items);
}
