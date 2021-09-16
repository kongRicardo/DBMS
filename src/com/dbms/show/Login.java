package com.dbms.show;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.dbms.server.*;


public class Login extends JFrame{
	JPanel jp1,jp2,jp3;
	JButton login, register;
	JLabel user, password;
	JTextField jtf1, jtf2;
	final  Container c = getContentPane();
	
	public Login() {
		jp1 = new JPanel();
		user = new JLabel("用户名");
		jtf1 = new JTextField(15);
		jp1.add(user);
		jp1.add(jtf1);
		
		jp2 = new JPanel();
		password = new JLabel("密  码");
		jtf2 = new JTextField(15);
		jp2.add(password);
		jp2.add(jtf2);
		
		jp3 = new JPanel();
		jp3.setLayout(new FlowLayout(1,30,5));
		login = new JButton("登录");
		register = new JButton("注册");
		jp3.add(login);
		jp3.add(register);
		
		c.setLayout(new GridLayout(5,1,10,10));
		c.add(new JPanel());
		c.add(jp1);
		c.add(jp2);
		c.add(jp3);
		c.add(new JPanel());
		
		this.setTitle("登录");
		this.setBounds(300,300,400,380);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	final JFrame jframe=this;
	public void init() {
		login.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String userName = jtf1.getText();
				String userPassword = jtf2.getText();
				User cc = new User(userName, userPassword);
				LoginCheck lc = new LoginCheck();
				try {
					if(lc.Check(cc)) {
						jframe.setVisible(false);
						new Client();
					}else 
						JOptionPane.showMessageDialog(null, "账号或密码错误","提示", JOptionPane.YES_NO_CANCEL_OPTION);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		register.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new Register();
				
			}
		});
	}
	
	public static void main(String[] args) {
		Login lg = new Login();
		lg.init();
	}
	
}


