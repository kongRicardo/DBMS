package com.dbms.server;

import java.io.*;

public class User implements Serializable {
	
	
	
	/**
	 * 
	 */
	 private static final long serialVersionUID = 8412493069374689821L;
	
	private String userName;
	private String userPassword;
	
	public User() {
		
	}
	
	public User(String userName, String userPassword) {
		this.userName = userName;
		this.userPassword = userPassword;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getUserPassword() {
		return userPassword;
	}
	
	public boolean pass(User cc) {
		if(this.userName == cc.userName && this.userPassword == cc.userPassword)
			return true;
		else
			return false;
	}
	
	public static void main(String[] args) {
		File f = new File("User\\ff.txt");
		//User cc = new User("cc","123");
		
		try {
			ObjectInputStream ois = new ObjectInputStream(
					new FileInputStream(f));
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream("User\\cc.txt"));
			//oos.writeObject(cc);
			try {
				User aa = (User)ois.readObject();
				System.out.println(aa.getUserName()+"   "+aa.getUserPassword());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
	}
}
