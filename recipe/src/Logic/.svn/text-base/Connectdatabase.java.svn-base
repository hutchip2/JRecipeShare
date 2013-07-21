package Logic;

import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;
import java.sql.SQLException;

public class Connectdatabase implements Runnable	{
	Thread t;
	public Connection connect = null;
	protected String query;
	protected ResultSet result=null;

	/**
	 * Run method for thread.
	 */
	public void run() {
	}

	/**
	 * Constructor for Connectdatabase class.
	 */
	public Connectdatabase()	{
		this.connectTodatabase();
	}

	/**
	 * Establishes a connection with the database.
	 */
	private void connectTodatabase()	{
		try	{
			Properties prop = new Properties();
			prop.load(new FileInputStream("identity.txt"));
			String username = prop.getProperty("username").trim();
			String password = prop.getProperty("password").trim();
			String url = prop.getProperty("databaseurl").trim();
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(url,username,password);
			//System.out.println("hahaaaaa I've established a connection");
		}
		catch (SQLException x) {
			System.out.println("Check the identity.txt file for correct values.");
		} 
		catch(Exception e) {
			System.out.println("bouh! Cannot connect to database");
			System.err.println(e);
			try	{			
				if(connect!=null) {
					connect.close();
				}
			} catch (SQLException e1){	
			}
		}
	}


	/**
	 * Executes a given SQL statement. 
	 * @param sql - String of SQL input statement
	 * @return null if sql statement failed.
	 */
	public ResultSet executeselect(String sql)	{
		ResultSet result = null;
		try {
			Statement stmt = connect.createStatement();
			stmt.execute(sql);
			result = stmt.getResultSet();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// closeconnection();
		return result;
	}

	/**
	 * Executes an insert statement from a sql string.
	 * @param insertsql
	 * @return True if the insert is successfully executed, false otherwise.
	 */
	public boolean executeInsert(String insertsql)	{
		boolean insertval = false;
		try	{
			Statement stmt = connect.createStatement();
			stmt.executeUpdate(insertsql);
			insertval = true;
		} catch (SQLException e)	{
			insertval = false;
			e.printStackTrace();
		}
		closeconnection();
		return insertval;
	}

	/**
	 * Closes the connection with the database.
	 */
	public void closeconnection()	{
		try	{
			connect.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
