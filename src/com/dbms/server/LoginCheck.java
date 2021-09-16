package com.dbms.server;

import java.io.*;

public class LoginCheck {
	private String PATH = "User\\";
	
	@SuppressWarnings("resource")
	public boolean Check(User cc) throws Exception{
		boolean ret = false;
		File f = new File(PATH);
		
		File[] fz = f.listFiles();

		
		for(int i=0 ; i<fz.length; i++) {
			
				ObjectInputStream ois = new ObjectInputStream(
						new FileInputStream(PATH+fz[i].getName())
						);
				User aa = (User)ois.readObject();
				if(aa.getUserName().equals(cc.getUserName())  &&
						aa.getUserPassword().equals(cc.getUserPassword())) {
					ret = true;
					break;
				} 
				
		}		
		return ret;
	}
	
	public String Register(String userName, String userPassword) throws Exception {
		String s = null;	
		File dir = new File(PATH);
		if(!dir.exists())
			dir.mkdir();
		User cc = new User(userName, userPassword);
		if( !Check(cc) ) {
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(PATH+userName+".txt"));
			oos.writeObject(cc);
			s = "成功";
		} else {
			s = "Existed";
			}
		return s;
	}
	
//	public static void main(String[] args) throws Exception {
//		LoginCheck lc = new LoginCheck();
//		User c = new User("bb","123");
//		System.out.println(lc.Check(c));
//	}
}
