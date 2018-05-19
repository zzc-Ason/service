package com.ason.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * author : Ason
 * createTime : 2018 年 05 月 19 日 12:51
 */
public class Client {

    public static <T> T getRemoteProxyObj(Class service, InetSocketAddress address) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, (Object proxy, Method method, Object[] args) -> {
            Socket socket = new Socket();
            socket.connect(address);

            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;
            try {
                // 向服务器发送请求
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeUTF(service.getName());   // 接口名
                outputStream.writeUTF(method.getName());    // 方法名
                outputStream.writeObject(method.getParameterTypes());     // 方法参数类型
                outputStream.writeObject(args);

                // 接受服务器相应结果
                inputStream = new ObjectInputStream(socket.getInputStream());
                return inputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
            return null;
        });
    }
}
