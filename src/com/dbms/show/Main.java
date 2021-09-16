package com.dbms.show;

import java.net.ServerSocket;
import java.net.Socket;

import com.dbms.server.*;

public class Main {
	
	

	public static void main(String[] args) throws Exception {
		showMain();
	}
	
	public static void showMain() throws Exception {
		//1.启动服务器
//        Server s = new Server();
//        ServerSocket ss = s.startServer();
        //2.启动客户端
		//Server.Main();
		new Server().start();
        new Client().showFrame();
        //3.连接客户端和服务器
//        Socket server = s.connect(ss);
//        s.recvSql(server);
        
        //4.登录
       
        /*
        while(true) {
            SQLManager sm = new SQLManager();
            //5.编写sql语句
            System.out.println();
            String sql = sm.createSql();
            //6.校检sql语句
            if(sm.checkSql(sql)) {
                //7.客户端发送sql语句
                c.sendSql(sql,client);
                //8.服务器接收sql语句
                String sqlRecv = s.recvSql(server);
                //9.解析接收的sql语句(分割sql语句，获得表和改动表的内容，操作文件)
                String msg = sm.parseSql(sqlRecv);
                //10.服务器发送操作结果
                s.sendMsg(msg,server);
                //11.客户端接收操作结果
                c.recvMsg(client);
            } else {
                System.out.println("sql语句错误！");
            }
        }
    }
	*/
	}

}
