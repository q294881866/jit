package io.seata.jit.server;

import io.seata.jit.ApplicationKeeper;
import io.seata.jit.Starter;
import io.seata.server.Server;

/**
 * @author ppf
 */
public class SeataServerStarter extends Starter {

    /**
     * The Server.
     */
    Server server = null;
    @Override
    protected void start0(String[] args) throws Exception {
        server = new Server();
        server.main(args);
        new ApplicationKeeper().keep();
    }
}
