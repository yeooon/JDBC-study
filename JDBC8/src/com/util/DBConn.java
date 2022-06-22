package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConn
{
	private static Connection dbConn;
	
	public static Connection getConnection()	
	{
		if (dbConn == null)
		{
			try
			{
				String url = "jdbc:oracle:thin:@localhost:1521:xe";	
				String user = "scott";
				String pw = "tiger";
				
				Class.forName("oracle.jdbc.driver.OracleDriver");
				
				dbConn = DriverManager.getConnection(url, user, pw);
				
			} catch (Exception e)
			{
				System.out.println(e.toString());
			}
		}
		return dbConn;
	}
	
	public static Connection getConnection(String url,String user,String pw) throws ClassNotFoundException, SQLException
	{
		if (dbConn == null)
		{
			try 
			{
				Class.forName("oracle.jdbc.driver.OracleDriver");		
				dbConn = DriverManager.getConnection(url,user,pw);
			}
			catch(Exception e)
			{
				System.out.print(e.toString());
			}
		}
		return dbConn;
	}
	
	public static void close()
	{
		if (dbConn != null)
		{
			try
			{
				if (dbConn.isClosed())
				{
					dbConn.close();
				}
			} catch (Exception e)
			{
				System.out.print(e.toString());
			}
		}
		dbConn = null;
	}
}
	
