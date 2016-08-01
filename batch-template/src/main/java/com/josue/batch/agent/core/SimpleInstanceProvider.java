package com.josue.batch.agent.core;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Josue on 02/05/2016.
 */
public class SimpleInstanceProvider implements InstanceProvider {

    private static final Logger logger = Logger.getLogger(SimpleInstanceProvider.class.getName());

    @Override
    public <T> T newInstance(Class<T> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
