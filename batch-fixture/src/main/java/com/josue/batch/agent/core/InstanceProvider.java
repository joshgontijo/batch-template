package com.josue.batch.agent.core;

/**
 * Created by Josue on 02/05/2016.
 */
public interface InstanceProvider {

    <T> T newInstance(Class<T> type);
}
