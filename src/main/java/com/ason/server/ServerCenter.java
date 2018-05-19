package com.ason.server;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author : Ason
 * createTime : 2018 年 05 月 19 日 12:46
 */
@Log4j2
public class ServerCenter implements Server {

    private static HashMap<String, Class> register = new HashMap<String, Class>();  // 注册容器
    private int port;   // 端口
    private static ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static boolean isRunning = false;   // 服务是否开启

    public ServerCenter(int port) {
        this.port = port;
    }

    public void start() {       // 启动服务
        try {
            ServerSocket server = new ServerSocket();
            server.bind(new InetSocketAddress(port));
            isRunning = true;   // 标记启动服务
            while (true) {
                log.info("[start server]");
                Socket socket = server.accept();    // 等待客户端请求
                executors.execute(() -> {       // 接收客户端每次请求，从连接池获取一个线程处理
                    ObjectOutputStream outputStream = null;
                    ObjectInputStream inputStream = null;
                    try {
                        inputStream = new ObjectInputStream(socket.getInputStream());
                        // 严格按照发送的顺序取值
                        String serviceName = inputStream.readUTF();
                        String methodName = inputStream.readUTF();
                        Class<?>[] paramTypes = (Class<?>[]) inputStream.readObject();
                        Object[] args = (Object[]) inputStream.readObject();

                        // 根据客户请求，找到具体接口
                        Class serviceClass = register.get(serviceName);
                        Method method = serviceClass.getMethod(methodName, paramTypes);
                        Object result = method.invoke(serviceClass.newInstance(), args);

                        // 将结果返回给客户端
                        outputStream = new ObjectOutputStream(socket.getOutputStream());
                        outputStream.writeObject(result);
                    } catch (Exception e) {
                        log.error("[server error] [e: {}]", e.getMessage());
                        e.printStackTrace();
                    } finally {
                        try {
                            if (outputStream != null) outputStream.close();
                            if (inputStream != null) inputStream.close();
                        } catch (IOException e) {
                            log.error("[close stream error] [e: {}]", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            log.error("[server error] [e: {}]", e.getMessage());
            e.printStackTrace();
        }

    }

    public void stop() {    // 关闭服务
        isRunning = false;
        executors.shutdown();
    }

    public void register(Class service, Class serviceImpl) {        // 注册服务
        register.put(service.getName(), serviceImpl);
    }

    public boolean isRunning() {    // 是否开启服务
        return isRunning;
    }
}
