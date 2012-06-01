package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateHelper;
import utils.Logger;

public class State implements IModel {
	
	private int id;
	private String name;
	private String alias;
	private int countryId;
	private Date createdAt;
	private Date updatedAt;
	
	private State(){
		this.id = 0;
	}
	
	public State(String name, Country country){
		this(name,"",country);
	}
	
	public State(String name, int countryId){
		this(name,"",countryId);
	}
	

	public State(String name, String alias, Country country){
		this(name,alias,country.getId());
	}
	
	public State(String name, String alias, int countryId){
		this.id = 0;
		this.name = name;
		this.alias = alias;
		this.countryId = countryId;
	}

	public static State create(String name, Country country) {
		return create(name,"",country);
	}
	
	public static State create(String name, int countryId) {
		return create(name,"",countryId);
	}
	
	public static State create(String name, String alias, Country country) {
		return create(name,"",country.getId());
	}
	
	public static State create(String name, String alias, int countryId) {
		State state = new State(name,alias, countryId);
		return state.save() ? state : null;
	}

	public int getId() {
		return id;
	}

	private void afterSave() {
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select max(id) from states");
			id = result.getInt(1);
			State state = State.find(id);
			this.createdAt = state.createdAt;
			this.updatedAt = state.updatedAt;
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
	
	public int getCountryId(){
		return countryId;
	}
	
	public void setCountryId(int countryId){
		this.countryId = countryId;
	}
	
	public Country getCountry(){
		return Country.find(countryId);
	}
	
	public void setCountry(Country country){
		countryId = country.getId();
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

	public static State find(int id){
		State state = new State();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select * from states where id = " + id);
			if(result.next()){
				state.id = id;
				state.name = result.getString("name");
				state.alias = result.getString("alias");
				state.countryId = result.getInt("country_id");
				state.createdAt = result.getDate("created_at");
				state.updatedAt = result.getDate("updated_at");
			}
			else
				Logger.warning("Couldn't find State with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search of State: "+e.toString()); }
		
		
		return state;
	}
	
	public static List<State> all(){
		List<State> states = new ArrayList<State>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from states");
			while(result.next()) states.add(find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of States: "+e.toString()); }
		return states;
	}
	
	public List<City> getCities(){
		List<City> cities = new ArrayList<City>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from cities where state_id="+id);
			while(result.next()) cities.add(City.find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Cities: "+e.toString()); }
		return cities;
	}
	
	@Override
	public boolean save() {
		String sql = "";
		if(id == 0) sql = "insert into states(`name`,`alias`,`country_id`,`created_at`,`updated_at`) values('"+name+"','"+alias+"',"+countryId+",'"+DateHelper.getDateTimeString(new Date())+"','"+DateHelper.getDateTimeString(new Date())+"')";
		else sql = "update states set name='"+name+"', alias='"+alias+"', country_id="+countryId+", updated_at='"+DateHelper.getDateTimeString(new Date())+"' where id="+id;
		try{Connection.getConnection().excecuteUpdate(sql); if(id == 0) afterSave(); return true;}
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try {Connection.getConnection().excecuteUpdate("delete from states where id="+id); return true;} 
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(State state : all()) state.destroy();
	}

	@Override
	public String attributes(){
		return "State [ id="+id+", name='"+name+"', alias='"+alias+"', country_id="+countryId+", " +
				"created_at="+DateHelper.getDateTimeString(createdAt)+", updated_at="+DateHelper.getDateTimeString(updatedAt)+" ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
