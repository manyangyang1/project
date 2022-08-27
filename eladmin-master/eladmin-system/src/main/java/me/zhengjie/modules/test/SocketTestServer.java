package me.zhengjie.modules.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
@Component
public class SocketTestServer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // 初始化服务端socket并且绑定9999端口
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("socket启动成功");
        //等待客户端的连接
        while (true){
            Socket socket = serverSocket.accept();
            //获取输入流
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //读取一行数据
            String str = bufferedReader.readLine();
            //输出打印
            System.out.println(str);
            BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            String str1="谢谢你，客户端";

            bufferedWriter.write(str1);
            bufferedWriter.flush();
            bufferedWriter.close();
        }


    }
}
