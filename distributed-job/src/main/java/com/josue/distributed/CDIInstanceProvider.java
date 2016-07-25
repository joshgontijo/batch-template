package com.josue.distributed;

import com.josue.batch.agent.core.InstanceProvider;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

/**
 * Created by Josue on 02/05/2016.
 */
public class CDIInstanceProvider implements InstanceProvider {

    @Override
    public <T> T newInstance(Class<T> type) {
        BeanManager beanManager = CDI.current().getBeanManager();
        Bean<T> bean = (Bean<T>) beanManager.getBeans(type).iterator().next();
        CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
        T instance = (T) beanManager.getReference(bean, type, ctx);

        return instance;
    }
}
