package models;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import schema.Tables;

public class Connection {
	public static Connection instance;
	private java.sql.Connection connection;
	
	private Connection(){
		try{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:J4Life.db");
			createTables();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(){
		if(instance == null) instance = new Connection();
		return instance;
	}
	
	public void createTables(){
		Tables tables = new Tables();
		for (String table : tables) {
			try{
				connection.createStatement().executeUpdate(table);
			}catch(SQLException e){
				e.printStackTrace();
				System.out.println(table);
			}
		}
	}
	
	public void excecuteUpdate(String sql){
		try {
			connection.createStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet excecuteQuery(String sql){
		try {
			return connection.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			return null;
		}
	}
}
