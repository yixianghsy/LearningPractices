package org.itstack.demo.design.user.impl;

import org.itstack.demo.design.user.User;
import org.itstack.demo.design.visitor.Visitor;

// 学生
public class Student extends User {

    public Student(String name, String identity, String clazz) {
        super(name, identity, clazz);
    }
    //在 accept ⽅法中，提供了本地对象的访问； visitor.visit(this) ，这块需要加深理解
    public void accept(Visitor visitor) {
        //把当前对象传进去，就可以根据类型调用
        visitor.visit(this);
    }

    public int ranking() {
        return (int) (Math.random() * 100);
    }

}
