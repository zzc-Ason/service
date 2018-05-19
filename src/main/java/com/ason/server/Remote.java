package com.ason.server;

import com.ason.util.Util;

/**
 * author : Ason
 * createTime : 2018 年 05 月 19 日 14:02
 */
public class Remote {

    private static final int PORT = 9999;

    public static void main(String[] args) {
        Util.load();
        new Thread(()->{
            Server server = new ServerCenter(PORT); // 打开端口连接
            Util.PROPS.forEach((k, v) -> {  // 循环注册
                try {
                    server.register(Class.forName(k), Class.forName(v));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            server.start(); // 启动服务器
        }).start();
    }
}
