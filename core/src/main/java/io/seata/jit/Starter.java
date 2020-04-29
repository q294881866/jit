package io.seata.jit;

/**
 * @author ppf
 */
public abstract class Starter {

    protected abstract void start0(String[] args) throws Exception;


    public void start(String[] args) throws Exception {
        start0(args);
        new ApplicationKeeper().keep();
    }
}
