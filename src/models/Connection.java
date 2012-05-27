package models;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;

import schema.Migrate;
import utils.Logger;

public class Connection {
	public static Connection instance;
	private java.sql.Connection connection;
	
	private Connection(){
		try{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:J4Life.db");
			Logger.fine("Database was Successfully Connected!");
			migrate();
		}catch(Exception e){
			Logger.severe("Error to Connect Database: ".concat(e.toString()));
		}
	}
	
	public static Connection getConnection(){
		if(instance == null) instance = new Connection();
		return instance;
	}
	
	public void migrate(){
		Migrate tables = new Migrate();
		for (Entry<String, String> table : tables.entrySet())
			try{
				connection.createStatement().executeUpdate(table.getValue());
				Logger.fine(table.getKey().concat(" table was successfully created."));
			}catch(SQLException e){
				if(e.getMessage().endsWith("already exists"))
					Logger.warning(e.toString());
				else
					Logger.severe(e.toString().concat(" by excecuting sentence: ").concat(table.getValue()));
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
