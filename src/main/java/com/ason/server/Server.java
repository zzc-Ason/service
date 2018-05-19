package com.ason.server;

/**
 * author : Ason
 * createTime : 2018 年 05 月 19 日 12:43
 */
public interface Server {

    void start();

    void stop();

    void register(Class service, Class serviceImpl);
}
