package com.josue.batch.agent.stage;

import com.josue.batch.agent.core.PropertyAware;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class StageChunkProcessor<T> extends PropertyAware {

    public abstract T proccess(T input);

}
