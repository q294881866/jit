package io.seata.jit;

/**
 * @author ppf
 */
public abstract class Tester {
    abstract Result test0();

    public void test(){
        System.out.println(test0());
    }
}
