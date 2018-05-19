package com.ason.service;

/**
 * author : Ason
 * createTime : 2018 年 05 月 19 日 12:38
 */
public class HelloImpl implements Hello {

    @Override
    public void sayHello() {
        System.out.println("no back");
    }

    public String sayHello(String name) {
        return "Hello: " + name;
    }
}
