/*
DBConn.java
-try ~ catch
*/

package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn
{
	private static Connection dbConn;
	
// 매개변수 없는 getConnection
	public static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		if(dbConn == null)
		{
			//데이터베이스 연결
			String url = "jdbc:oracle:thin:@localhost:1521:xe";	
			String user = "scott"; 
			String pw = "tiger";
		
			Class.forName("oracle.jdbc.driver.OracleDriver");
			dbConn = DriverManager.getConnection(url, user, pw);	
		}	
		return dbConn;
	}
	
	public static Connection getConnection(String url, String user, String pw) throws ClassNotFoundException, SQLException
	{
		if (dbConn == null)
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			dbConn = DriverManager.getConnection(url, user, pw);
		}
		return dbConn;
	}
	
	public static void close() throws SQLException
	{
		if (dbConn != null)
		{
			if (dbConn.isClosed())
			{
				dbConn.close();
			}
		}
		dbConn = null;
	}
}





/*
public class DBConn
{
	private static Connection dbConn;
	
	// 매개변수 없는 getConnection() 메소드
	public static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		if (dbConn == null)
		{
			String url = "jdbc:oracle:thin:@localhost:1521:xe";	
			String user = "scott";
			String pw = "tiger";
			
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			dbConn = DriverManager.getConnection(url, user, pw);
			
		}
	return dbConn;	
	}
	
	// 매개변수 있는 getConnection() 메소드
	public static Connection getConnection(String url, String user, String pw) throws ClassNotFoundException, SQLException
	{
		if (dbConn == null)
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			dbConn = DriverManager.getConnection(url, user, pw);
		}
		return dbConn;
	}
	
	// 닫는 거
	public static void close() throws SQLException
	{
		if (dbConn != null)
		{
			if (dbConn.isClosed())
			{
				dbConn.close();
			}
		}
		dbConn = null;
	}
}
*/
