package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateHelper;
import utils.Logger;

public class City implements IModel {
	
	private int id;
	private String name;
	private String alias;
	private int stateId;
	private Date createdAt;
	private Date updatedAt;
	
	private City(){
		this.id = 0;
	}
	
	public City(String name, State state){
		this(name,"",state);
	}
	
	public City(String name, int stateId){
		this(name,"",stateId);
	}
	

	public City(String name, String alias, State state){
		this(name,alias,state.getId());
	}
	
	public City(String name, String alias, int stateId){
		this.id = 0;
		this.name = name;
		this.alias = alias;
		this.stateId = stateId;
	}

	public static City create(String name, State state) {
		return create(name,"",state);
	}
	
	public static City create(String name, int stateId) {
		return create(name,"",stateId);
	}
	
	public static City create(String name, String alias, State state) {
		return create(name,"",state.getId());
	}
	
	public static City create(String name, String alias, int stateId) {
		City city = new City(name,alias, stateId);
		return city.save() ? city : null;
	}

	public int getId() {
		return id;
	}

	private void setId() {
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select max(id) from cities");
			id = result.getInt(1);
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
	
	public int getStateId(){
		return stateId;
	}
	
	public void setStateId(int stateId){
		this.stateId = stateId;
	}
	
	public State getState(){
		return State.find(stateId);
	}
	
	public void setState(State state){
		stateId = state.getId();
	}

	public static City find(int id){
		City city = new City();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select * from cities where id = " + id);
			if(result.next()){
				city.id = id;
				city.name = result.getString("name");
				city.alias = result.getString("alias");
				city.stateId = result.getInt("state_id");
			}
			else
				Logger.warning("Couldn't find City with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search of City: "+e.toString()); }
		
		
		return city;
	}
	
	public static List<City> all(){
		List<City> cities = new ArrayList<City>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from cities");
			while(result.next()) cities.add(find(result.getInt("id")));
			
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Cities: "+e.toString()); }
		return cities;
	}
	
	@Override
	public boolean save() {
		String sql = "";
		if(id == 0) sql = "insert into cities(`name`,`alias`,`state_id`) values('"+name+"','"+alias+"',"+stateId+")";
		else sql = "update from cities set name="+name+", alias="+alias+", state_id="+stateId+" where id="+id;
		try{Connection.getConnection().excecuteUpdate(sql); if(id == 0) setId(); return true;}
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try {Connection.getConnection().excecuteUpdate("delete from cities where id="+id); return true;} 
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(City city : all()) city.destroy();
	}

	@Override
	public String attributes(){
		return "City [ id="+id+", name='"+name+"', alias='"+alias+"', state_id="+stateId+", " +
				"created_at="+DateHelper.getDateString(createdAt)+", updated_at="+DateHelper.getDateString(updatedAt)+" ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
