package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateHelper;
import utils.Logger;

public class Country implements IModel {
	
	private int id;
	private String name;
	private String alias;
	private Date createdAt;
	private Date updatedAt;
	
	private Country(){
		this.id = 0;
	}
	
	public Country(String name){
		this(name,"");
	}
	
	public Country(String name, String alias){
		this.id = 0;
		this.name = name;
		this.alias = alias;
	}

	public static Country create(String name) {
		return create(name,"");
	}
	
	public static Country create(String name, String alias) {
		Country country = new Country(name,alias);
		return country.save() ? country : null;
	}

	public int getId() {
		return id;
	}

	private void afterSave() {
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select max(id) from countries");
			id = result.getInt(1);
			Country country = Country.find(id);
			this.createdAt = country.createdAt;
			this.updatedAt = country.updatedAt;
		} catch (SQLException e) {}
	}
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public static Country find(int id){
		Country country = new Country();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select * from countries where id="+id);
			if(result.next()){
				country.id = id;
				country.name = result.getString("name");
				country.alias = result.getString("alias");
				country.createdAt = result.getDate("created_at");
				country.updatedAt = result.getDate("updated_at");
			}
			else
				Logger.warning("Couldn't find Country with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search of Country: "+e.toString()); }
		
		
		return country;
	}
	
	public static List<Country> all(){
		List<Country> countries = new ArrayList<Country>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from countries");
			while(result.next()) countries.add(find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Country: "+e.toString()); }
		return countries;
	}
	
	public List<State> getStates(){
		List<State> states = new ArrayList<State>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from states where country_id="+id);
			while(result.next()) states.add(State.find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of States: "+e.toString()); }
		return states;
	}
	
	public List<City> getCities(){
		List<City> cities = new ArrayList<City>();
		for(State state : getStates()) cities.addAll(state.getCities());
		return cities;
	}
	
	@Override
	public boolean save() {
		String sql = "";
		if(id == 0) sql = "insert into countries(`name`,`alias`,`created_at`,`updated_at`) values('"+name+"','"+alias+"','"+DateHelper.getDateTimeString(new Date())+"','"+DateHelper.getDateTimeString(new Date())+"')";
		else sql = "update countries set name='"+name+"', alias='"+alias+"', updated_at='"+DateHelper.getDateTimeString(new Date())+"' where id="+id;
		try{Connection.getConnection().excecuteUpdate(sql); if(id == 0) afterSave(); return true;}
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try {Connection.getConnection().excecuteUpdate("delete from countries where id="+id); return true;} 
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(Country country : all()) country.destroy();
	}

	@Override
	public String attributes(){
		return "Country [ id="+id+", name='"+name+"', alias='"+alias+"', created_at='"+DateHelper.getDateTimeString(createdAt)+"', updated_at='"+DateHelper.getDateTimeString(updatedAt)+"' ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
