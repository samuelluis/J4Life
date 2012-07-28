package models;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map.Entry;

import schema.Migrate;
import schema.Seed;
import utils.Logger;

public class Connection {
	private static Connection instance;
	private java.sql.Connection connection;
	
	private Connection(){
		connect();
		migrate();
		close();
	}
	
	public static Connection getConnection(){
		if(instance == null) instance = new Connection();
		return instance;
	}
	
	public void migrate(){
		Migrate tables = new Migrate();
		boolean runSeed = false;
		for (Entry<String, String> table : tables.entrySet())
			try{
				connection.createStatement().executeUpdate(table.getValue());
				Logger.fine(table.getKey().concat(" table was successfully created."));
				runSeed = true;
			}catch(SQLException e){
				if(e.getMessage().endsWith("already exists"))
					Logger.warning(e.toString());
				else
					Logger.severe(e.toString().concat(" by excecuting sentence: ").concat(table.getValue()));
			}
		if(runSeed) Seed.seed();
	}
	
	public boolean executeUpdate(String sql){
		return executeUpdate(sql, null);
	}
	
	public ResultSet executeQuery(String sql){
		return executeQuery(sql, null);
	}
	
	public boolean executeUpdate(String sql, String catchStr){
		boolean result = false;
		try {
			connect();
			connection.createStatement().executeUpdate(sql);
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		}
		finally{
			close();
		}
		return result;
	}
	
	public ResultSet executeQuery(String sql, String catchStr){
		try {
			connect();
			return connection.createStatement().executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			close();
		}
		return null;
	}
	
	public void connect(){
		try{
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:J4Life.db");
			Logger.fine("Database was Successfully Connected!");
		}catch(Exception e){
			Logger.severe("Error to Connect Database: ".concat(e.toString()));
		}
	}
	
	public void close(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
