package org.jit.util;

/**
 * @author ppf
 */
public abstract class Tester {
    abstract Result test0();

    public void test(){
        System.out.println(test0());
    }
}
