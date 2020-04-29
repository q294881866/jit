package org.jit.util;

/**
 * @author ppf
 */
public abstract class Starter {

    abstract void start0();

    public void start(){
        start0();
        new ApplicationKeeper().keep();
    }
}
