package com.ason.client;

import com.ason.service.Hello;

import java.net.InetSocketAddress;

/**
 * author : Ason
 * createTime : 2018 年 05 月 19 日 14:04
 */
public class ClientTest {

    public static void main(String[] args) throws ClassNotFoundException {
        Hello result = Client.getRemoteProxyObj(Class.forName("com.ason.service.Hello"), new InetSocketAddress("47.94.240.103", 9999));
        System.out.println(result.sayHello("Tom"));;
    }
}
