package com.josue.distributed.master;

import org.wildfly.swarm.container.Container;

/**
 * @author Josue Gontijo <josue.gontijo@maersk.com>.
 */
public class Main {

    public static void main(String... args) throws Exception {
        Container container = new Container(args);
        container.start();
        container.deploy();
    }
}
