package io.seata.jit.action.impl;

import io.seata.jit.action.ResultHolder;
import io.seata.jit.action.TccActionOne;
import io.seata.rm.tcc.api.BusinessActionContext;

/**
 * The type Tcc action one.
 *
 * @author zhangsen
 */
public class TccActionOneImpl implements TccActionOne {

    @Override
    public boolean prepare(BusinessActionContext actionContext, int a) {
        String xid = actionContext.getXid();
        System.out.println("TccActionOne prepare, xid:" + xid +  ", a:" + a);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println("TccActionOne commit, xid:" + xid + ", a:" + actionContext.getActionContext("a"));
        ResultHolder.setActionOneResult(xid, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        System.out.println("TccActionOne rollback, xid:" + xid + ", a:" + actionContext.getActionContext("a"));
        ResultHolder.setActionOneResult(xid, "R");
        return true;
    }
}
