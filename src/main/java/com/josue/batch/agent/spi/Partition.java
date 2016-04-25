package com.josue.batch.agent.spi;

import java.util.Properties;

/**
 * Created by Josue on 19/04/2016.
 */
public class Partition {

    public Properties[] setPartitionProperties(int partitions) {
        int itemPerPartition = 10;
        Properties[] props = new Properties[partitions];
        for (int i = 0; i < partitions; i++) {
            props[i] = new Properties();
            props[i].setProperty("start", String.valueOf(i * itemPerPartition + 1));
            props[i].setProperty("end", String.valueOf((i + 1) * itemPerPartition));
        }
        return props;

    }
}
