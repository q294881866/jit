package io.seata.jit;

/**
 * @author ppf
 */
public abstract class Starter {

    protected abstract void start0() throws Exception;


    public void start() throws Exception {
        start0();
        new ApplicationKeeper().keep();
    }
}
