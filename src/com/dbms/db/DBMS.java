package com.dbms.db;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.dbms.operate.*;
import com.linuxense.javadbf.DBFField;
import com.dbms.dbf.*;

public class DBMS {

	public static final String DATA_PATH = "data\\";// 数据库存储路径
	public static final String LOG_PATH = "logs\\";// 系统日志存储路径
	private static File constraintFile;// 数据字典文件
	

	static {
		String constraint = DATA_PATH + "constraint.dbf";
		constraintFile = new File(constraint);
		if (!constraintFile.exists())
			try {
				constraintFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public DBMS() {
	
	}

	// 根据sql语句开头判断操作类型
	public String parseSQL(String sql) throws MyException {
		String ret = null;
		if (sql.trim().startsWith("create")) {
			Create create = parseCreate(sql);
			DBFContent content = create.exccuteSQL();
			ret =  "建表成功";
			recordSystemLogs(sql);
		} else if (sql.trim().toLowerCase().startsWith("insert")) {
			Insert insert = parseInsert(sql);
			DBFContent content = insert.excuteSQL();
			ret =  "插入成功";
			recordSystemLogs(sql);
		} else if (sql.trim().toLowerCase().startsWith("delete")) {
			Delete delete = parseDelete(sql);
			DBFContent content = delete.excuteSQL();
			ret =  "删除成功";
			recordSystemLogs(sql);
		} else if (sql.trim().toLowerCase().startsWith("update")) {
			Update update = parseUpdate(sql);
			DBFContent content = update.excuteSQL();
			ret =  "修改成功";
			recordSystemLogs(sql);
		} else if (sql.trim().toLowerCase().startsWith("select")) {
			Select selectSql = parseSelect(sql);
			DBFContent content = selectSql.excuteSQL();
			showQuery(content);
			ret = "查询成功";
			recordSystemLogs(sql);
		} else if (sql.trim().toLowerCase().startsWith("alter")) {
			Alter alter = parseAlter(sql);
			String title = alter.excuteSQL();
			ret = "修改成功";

			recordSystemLogs(sql);
		} else if (sql.trim().toLowerCase().startsWith("drop")) {
			Drop drop = parseDrop(sql);
			String title = drop.excuteSQL();
			ret = "删除成功";

			recordSystemLogs(sql);
		}
		
		return ret;
	}

	// 记录系统日志
	public void recordSystemLogs(String sql) {
		File file = new File(LOG_PATH + "logs.log");
		FileOutputStream outputStream = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			outputStream = new FileOutputStream(file, true);
			Date date = new Date(System.currentTimeMillis());// 获取系统时间
			outputStream
					.write((date.toLocaleString() + " -->> : " + sql + "\n")
							.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				outputStream = null;
			}
		}
	}

	// Insert语句预处理，去除空白符，分号，根据语句特点分解提取语句信息
	public Insert parseInsert(String sql) throws MyException {
		sql = sql.toLowerCase();// 变小写
		sql = sql.replaceAll("[\\s]{1,}", " ");// 将连续的多个空白符替换成单个空格
		// sql=""+sql+" ENDOFSQL";
		if (sql.endsWith(";"))// 去除分号
			sql = sql.substring(0, sql.length() - 1);
		sql = sql.trim();// 去除首尾空格

		String insert = sql.substring(0, sql.indexOf(" "));
		if (!"insert".equals(insert)) {
			// insert拼写错误，抛出异常
			throw new MyException("insert拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String into = sql.substring(0, sql.indexOf(" "));
		if (!"into".equals(into)) {
			// into拼写错误，抛出异常
			throw new MyException("into拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String tableName = sql.substring(0, sql.indexOf("(")).trim();
		String fieldString = sql.substring(sql.indexOf("(") + 1,
				sql.indexOf(")"));
		sql = sql.substring(sql.indexOf(")") + 1).trim();
		String values = sql.substring(0, sql.indexOf(" "));
		if (!"values".equals(values)) {
			// values拼写错误，抛出异常
			throw new MyException("values拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String valueString = sql.substring(sql.indexOf("(") + 1,
				sql.indexOf(")"));
		return new Insert(tableName, fieldString, valueString);
	}

	// Create语句预处理，去除空白符，分号，根据语句特点分解提取语句信息
	public Create parseCreate(String sql) throws MyException {
		sql = sql.toLowerCase();
		sql = sql.replaceAll("[\\s]{1,}", " ");
		// sql=""+sql+" ENDOFSQL";
		if (sql.endsWith(";"))
			sql = sql.substring(0, sql.length() - 1);
		sql = sql.trim();

		String create = sql.substring(0, sql.indexOf(" "));
		if (!"create".equals(create)) {
			// create拼写错误，抛出异常
			throw new MyException("create拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String table = sql.substring(0, sql.indexOf(" "));
		if (!"table".equals(table)) {
			// table拼写错误，抛出异常
			throw new MyException("table拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String tableName = sql.substring(0, sql.indexOf("(")).trim();
		String fieldsString = sql.substring(sql.indexOf("(") + 1,
				sql.length() - 1);
		String[] fieldsEntry = fieldsString.split(",");
		List<String[]> list = new ArrayList<String[]>();
		for (String fieldChild : fieldsEntry) {
			String[] field = fieldChild.split(" ");
			list.add(field);
		}
		return new Create(list, tableName);
	}

	// Select语句预处理，去除空白符，分号，根据语句特点分解提取语句信息
	public static Select parseSelect(String sql) throws MyException {
		sql = sql.trim();
		sql = sql.toLowerCase();
		sql = sql.replaceAll("[\\s|,]{1,}", " ");
		// sql=""+sql+" ENDOFSQL";

		if (sql.endsWith(";"))
			sql = sql.substring(0, sql.length() - 1);
		return new Select(sql.split(" "));
	}

	// Delete语句预处理，去除空白符，分号，根据语句特点分解提取语句信息
	public Delete parseDelete(String sql) throws MyException {
		sql = sql.toLowerCase();
		sql = sql.replaceAll("[\\s]{1,}", " ");
		// sql=""+sql+" ENDOFSQL";
		if (sql.endsWith(";"))
			sql = sql.substring(0, sql.length() - 1);
		sql = sql.trim();

		String delete = sql.substring(0, sql.indexOf(" "));
		System.out.println(delete);
		if (!"delete".equals(delete)) {
			// delete拼写错误，抛出异常
			throw new MyException("delete拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String from = sql.substring(0, sql.indexOf(" "));
		System.out.println(from);
		if (!"from".equals(from)) {
			// from拼写错误，抛出异常
			throw new MyException("from拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String tableName = sql.substring(0, sql.indexOf(" ")).trim();
		System.out.println(tableName);
		String whereString = null;
		if (sql.contains("where")) {
			sql = sql.substring(sql.indexOf(" ") + 1);
			String where = sql.substring(0, sql.indexOf(" ")).trim();
			System.out.println(where);
			if (!"where".equals(where)) {
				// where拼写错误，抛出异常
				throw new MyException("where拼写错误");
			}
			sql = sql.substring(sql.indexOf(" ") + 1);
			whereString = sql.trim();
			System.out.println(whereString);
		}
		return new Delete(tableName, whereString);
	}

	// Update语句预处理，去除空白符，分号，根据语句特点分解提取语句信息
	public Update parseUpdate(String sql) throws MyException {
		sql = sql.toLowerCase();
		sql = sql.replaceAll("[\\s]{1,}", " ");
		// sql=""+sql+" ENDOFSQL";
		if (sql.endsWith(";"))
			sql = sql.substring(0, sql.length() - 1);
		sql = sql.trim();

		String update = sql.substring(0, sql.indexOf(" "));
		System.out.println(update);
		if (!"update".equals(update)) {
			// update拼写错误，抛出异常
			throw new MyException("update拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String tableName = sql.substring(0, sql.indexOf(" "));
		System.out.println(tableName);
		sql = sql.substring(sql.indexOf(" ") + 1);
		String set = sql.substring(0, sql.indexOf(" ")).trim();
		System.out.println(set);
		if (!"set".equals(set)) {
			// set拼写错误，抛出异常
			throw new MyException("set拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		if (!sql.contains("where")) {
			// 没有where限制条件，不符合update，抛出异常
			throw new MyException("Update没有Where语句限制条件");
		}
		String updateString = sql.substring(0, sql.indexOf("where")).trim();
		System.out.println(updateString);

		String whereString = sql.substring(sql.indexOf("where") + 5).trim();
		System.out.println(whereString);

		return new Update(tableName, updateString, whereString);
	}

	// Alter语句预处理，去除空白符，分号，根据语句特点分解提取语句信息
	public Alter parseAlter(String sql) throws MyException {
		sql = sql.toLowerCase();
		sql = sql.replaceAll("[\\s]{1,}", " ");
		// sql=""+sql+" ENDOFSQL";
		if (sql.endsWith(";"))
			sql = sql.substring(0, sql.length() - 1);
		sql = sql.trim();

		String alter = sql.substring(0, sql.indexOf(" "));
		System.out.println(alter);
		if (!"alter".equals(alter)) {
			// alter拼写错误，抛出异常
			throw new MyException("alter拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String table = sql.substring(0, sql.indexOf(" "));
		System.out.println(table);
		if (!"table".equals(table)) {
			// table拼写错误，抛出异常
			throw new MyException("table拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String tableName = sql.substring(0, sql.indexOf(" ")).trim();
		System.out.println(tableName);
		sql = sql.substring(sql.indexOf(" ") + 1);
		String type = sql.substring(0, sql.indexOf(" ")).trim();
		System.out.println(type);
		sql = sql.substring(sql.indexOf(" ") + 1);
		System.out.println(sql);
		return new Alter(tableName, type, sql);
	}

	// Drop语句预处理，去除空白符，分号，根据语句特点分解提取语句信息
	public Drop parseDrop(String sql) throws MyException {
		sql = sql.toLowerCase();
		sql = sql.replaceAll("[\\s]{1,}", " ");
		// sql=""+sql+" ENDOFSQL";
		if (sql.endsWith(";"))
			sql = sql.substring(0, sql.length() - 1);
		sql = sql.trim();

		String drop = sql.substring(0, sql.indexOf(" "));
		System.out.println(drop);
		if (!"drop".equals(drop)) {
			// drop拼写错误，抛出异常
			throw new MyException("drop拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String table = sql.substring(0, sql.indexOf(" "));
		System.out.println(table);
		if (!"table".equals(table)) {
			// table拼写错误，抛出异常
			throw new MyException("table拼写错误");
		}
		sql = sql.substring(sql.indexOf(" ") + 1);
		String tableName = sql.trim();
		System.out.println(tableName);

		return new Drop(tableName);
	}
	
	public void showQuery(DBFContent content) {
		JFrame frame = new JFrame();
		JPanel panel = new JPanel();
		
		frame.setBounds(100,200,400,600);
		
		
		
		
		JTextArea show = new JTextArea();
		show.setBackground(Color.white);
		show.setLineWrap(true);
		show.setEditable(false);
		show.setBounds(100,100,400,600);
		
		StringBuilder builder = new StringBuilder();
		builder.append("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		List<DBFField> fields = content.getFields();
		for (int i = 0; i < fields.size(); i++) {
			if (i == fields.size() - 1) {
				builder.append("      ").append(fields.get(i).getName())
						.append("\n");
			} else
				builder.append("      ").append(fields.get(i).getName())
						.append("       |");
		}
		builder.append("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		for (int i = 0; i < content.getRecordCount(); i++) {
			Map<String, Object> map = content.getContents().get(i);
			for (int j = 0; j < fields.size(); j++) {
				if (j == fields.size() - 1) {
					builder.append("     ")
							.append(map.get(fields.get(j).getName()))
							.append("\n");
				} else
					builder.append("     ")
							.append(map.get(fields.get(j).getName()))
							.append("     |");
			}
			builder.append("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		}
		show.setText(builder.toString());
		
		panel.add(show);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) throws MyException {
		DBMS root = new DBMS();
		Date d1 = new Date();
		for(int i= 0; i<100; i++) {
			
			String sql = "create table student"+(10+i)+"(id int,name char(10),age int);";
			String in = "insert into student"+(10+i)+"(id,name,age) values (1,'a',1);";
			String d = "drop table student"+(10+i);
			
			String ret = root.parseSQL(sql);
			String ret2 = root.parseSQL(in);
			String ret3 = root.parseSQL(d);
			
			//System.out.println(ret+"  "+ret2+"  "+ret3);

		}
		Date d2 = new Date();
		System.out.println(d2.getTime() - d1.getTime());
	}
}
