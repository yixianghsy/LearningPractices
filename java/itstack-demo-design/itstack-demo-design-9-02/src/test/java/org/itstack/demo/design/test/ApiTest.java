package org.itstack.demo.design.test;

import org.itstack.demo.design.LoginSsoDecorator;
import org.itstack.demo.design.SsoInterceptor;
import org.junit.Test;

/**
 * 装饰器的核⼼就是再不改原有类的基础上 )
 * 给类新增功能。不改变原有类，可能有的⼩伙伴会想到继承、AOP切⾯，当然这些⽅式都可以实现，但
 * 是使⽤装饰器模式会是另外⼀种思路更为灵活，可以避免继承导致的⼦类过多，也可以避免AOP带来的
 * 复杂性
 * 装饰器模式
 * 根据传入的对象决定执行某个子类实现，有点类似决策模式
 * 使⽤装饰器模式满⾜单⼀职责原则，你可以在⾃⼰的装饰类中完成功能逻辑的扩展，⽽不影响主
 * 类，同时可以按需在运⾏时添加和删除这部分逻辑。另外装饰器模式与继承⽗类᯿写⽅法，在某些
 * 时候需要按需选择，并不⼀定某⼀个就是最好。
 * 装饰器实现的᯿点是对抽象类继承接⼝⽅式的使⽤，同时设定被继承的接⼝可以通过构造函数传递
 * 其实现类，由此增加扩展性并᯿写⽅法⾥可以实现此部分⽗类实现的功能。
 * 就像夏天热你穿短裤，冬天冷你穿棉裤，⾬天挨浇你穿⾬⾐⼀样，你的根本本身没有被改变，⽽你
 * 的需求却被不同的装饰⽽实现。⽣活中往往⽐⽐皆是设计，当你可以融合这部分活灵活现的例⼦到
 * 代码实现中，往往会创造出更加优雅的实现⽅式
 */
public class ApiTest {

    @Test
    public void test_LoginSsoDecorator() {
        LoginSsoDecorator ssoDecorator = new LoginSsoDecorator(new SsoInterceptor());
        String request = "1successhuahua";
        boolean success = ssoDecorator.preHandle(request, "ewcdqwt40liuiu", "t");
        System.out.println("登录校验：" + request + (success ? " 放行" : " 拦截"));
    }

}
