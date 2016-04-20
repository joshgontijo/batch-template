package com.josue.batch.agent;

import java.io.Serializable;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class ItemReader<T> extends Distributed implements Serializable {

    protected abstract T read();
}
