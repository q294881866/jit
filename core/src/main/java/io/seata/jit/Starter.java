package io.seata.jit;

/**
 * @author ppf
 */
public abstract class Starter {

    protected abstract void start0(String[] args) throws Exception;


    public void start(String[] args) {
        try {
            start0(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new ApplicationKeeper().keep();
    }
}
