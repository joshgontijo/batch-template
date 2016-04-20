package com.josue.batch.agent;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Josue on 19/04/2016.
 */
public abstract class ItemWriter<T> extends Distributed implements Serializable {

    protected abstract void write(List<T> items);
}
