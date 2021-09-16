package com.dbms.show;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.*;

public class c2 {
	//
	PrintWriter pw;
	Socket socket;
	//
	JFrame frame = new JFrame("123");
	JPanel panel = new JPanel();
	JButton btn = new JButton("send");
	private JTextArea area_showWindow;
	private JTextArea area_inputWindow;
	
	public c2() {
		try {
			socket = new Socket("localhost",1234);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		frame.setContentPane(panel);
		
		//
		area_inputWindow = new JTextArea(60,60);
		area_showWindow = new JTextArea(60,60);
		
		//
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String s = area_inputWindow.getText();
				area_showWindow.setText(s);
				//
				socket(s);
			}
		});
		
		
		//
		panel.add(area_inputWindow);
		panel.add(area_showWindow);
		panel.add(btn);
		
		//socket();
		
		//
		frame.pack();
		frame.setVisible(true);
	}
	
	public void socket(String s){
		try {
			
			
			InputStreamReader isr=new InputStreamReader(socket.getInputStream());
			BufferedReader br=new BufferedReader(isr);
			
			PrintWriter pw=new PrintWriter(socket.getOutputStream(),true);
			pw.println(s);
			
			
				//不停地读取从服务器端发来的信息
				String info=br.readLine();
				area_showWindow.append("服务端： "+info+"\r\n");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new c2();
	}
}
