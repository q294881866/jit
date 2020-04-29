package io.seata.jit.server;

import io.seata.jit.Starter;
import io.seata.server.Server;

import java.io.IOException;

/**
 * @author ppf
 */
public class SeataServerStarter extends Starter {

    /**
     * The Server.
     */
    Server server = null;
    @Override
    protected void start0() throws Exception {
        server = new Server();
        String[] args = new String[3];
        server.main(args);
    }
}
