package org.itstack.demo.design.event.listener;

import org.itstack.demo.design.LotteryResult;
//事件监听接⼝定义
public interface EventListener {

    void doEvent(LotteryResult result);

}
