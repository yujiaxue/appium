package com.persist.api;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcConnection {

	String url = "jdbc:mysql//127.0.0.1:3306/hxAndroid";

	String driverClass = null;
	String user = null;
	String passwd = null;
	private static Connection jc = null;

	public static synchronized Connection getConn() {
		if (jc == null) {
			jc = new JdbcConnection().getConnection();
		} else {
			return jc;
		}
		return jc;
	}


	private Connection getConnection() {
		Connection con = null;
		InputStream in = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties pro = new Properties();
		try {
			pro.load(in);
			driverClass = pro.getProperty("driverClass");
			url = pro.getProperty("url");
			user = pro.getProperty("user");
			passwd = pro.getProperty("password");
			// Driver driver = (Driver)
			// Class.forName(driverClass).newInstance();
//			Properties p = new Properties();
//			p.put("user", user);
//			p.put("password", passwd);
			Class.forName(driverClass);
			//con = DriverManager.getConnection(url, user, passwd);
			con = DriverManager.getConnection(url, user, passwd);

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return con;
	}
}
