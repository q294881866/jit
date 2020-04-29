package io.seata.jit.server;

import io.seata.jit.Starter;
import org.apache.curator.test.TestingServer;

/**
 * @author ppf
 */
public class ZookeeperStarter extends Starter {
    private static TestingServer server;
    @Override
    protected void start0(String[] args) throws Exception {
        server = new TestingServer(2181, true);
        server.start();
    }
}
