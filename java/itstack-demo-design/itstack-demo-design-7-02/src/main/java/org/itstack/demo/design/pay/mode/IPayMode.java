package org.itstack.demo.design.pay.mode;

/**
 *  定义⽀付模式接⼝
 */
public interface IPayMode {

    boolean security(String uId);

}
