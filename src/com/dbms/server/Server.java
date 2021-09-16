package com.dbms.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.dbms.db.*;
import com.dbms.operate.MyException;

public class Server {

    public Server() {

    }

    //启动服务器
    public ServerSocket startServer() throws IOException {

        //创建ServerSocket对象，等待连接
        ServerSocket ss = new ServerSocket(6666);
        System.out.println("服务器已启动");
        return ss;
    }

    //连接服务器和客户端
    public Socket connect(ServerSocket ss) throws IOException {

        //接收连接
        Socket server = ss.accept();
        return server;
    }
    

    //接收sql语句
    public String recvSql(Socket server) throws IOException {

        BufferedReader is = new BufferedReader(
                new InputStreamReader(server.getInputStream()));
        String sql = is.readLine();
        System.out.println("服务器：zzz");	
        return sql;
    }

    //发送操作结果
    public void sendMsg(String msg,Socket server) throws IOException {

        OutputStream os = server.getOutputStream();
        PrintWriter pw = new PrintWriter(os, true);
        msg += "\r\n"+" "; //数据结束标识
        pw.println(msg);
    }
   /* 
    public void recMsg(Socket server) throws IOException {
        BufferedReader bf = new BufferedReader(
                new InputStreamReader(server.getInputStream()));
        PrintWriter pw = new PrintWriter(server.getOutputStream(),true);
        
        while(true) {
        	String info = bf.readLine();
        	System.out.println("服务器："+info);
        }
    }
    */
    public static void start() throws Exception{
    	ServerSocket ss = new ServerSocket(1234);
        System.out.println("服务器已启动");
        Socket server = ss.accept();
        
        String info;
        
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader is = new BufferedReader(new InputStreamReader(server.getInputStream()));
        PrintWriter os = new PrintWriter(server.getOutputStream(), true);
        
        while(true) {
        	String sql = null;
        	sql = is.readLine();
        	System.out.println("Client:" + sql);
        
        	DBMS root = new DBMS();
        	String ret = root.parseSQL(sql);
        	System.out.println(ret);
        	os.println(ret);
        	os.flush();
        }
 
    }
    
    
    public static void main(String[] args) throws IOException, MyException {
    	ServerSocket ss = new ServerSocket(1234);
        System.out.println("服务器已启动");
        Socket server = ss.accept();
        
        String info;
        
        BufferedReader sin = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader is = new BufferedReader(new InputStreamReader(server.getInputStream()));
        PrintWriter os = new PrintWriter(server.getOutputStream(), true);
        
        while(true) {
        	

        	String sql = null;
        	sql = is.readLine();
        	System.out.println("Client:" + sql);
        
        	DBMS root = new DBMS();
        	String ret = root.parseSQL(sql);
        	//System.out.println(ret);
        	os.println(ret);
        	os.flush();
        }
 
    }

}
