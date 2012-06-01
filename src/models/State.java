package models;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utils.Logger;

public class State implements IModel {
	
	private int id;
	private String name;
	private String alias;
	private int countryId;
	
	private State(){
		this(null, null, 0);
	}
	
	public State(String name, String alias, Country country){
		this(name,alias,country.getId());
	}
	
	public State(String name, String alias, int countryId){
		super();
		this.id = 0;
		this.name = name;
		this.alias = alias;
		this.countryId = countryId;
	}
	public static State create() {
		return create(null, null, 0);
	}
	public static State create(String name,String alias,int countryId) {
		State state = new State( name, alias, countryId);
		return state.save() ? state : null;
	}
	
	public int getId() {
		return id;
	}
	public void setId() {
		try { 
			ResultSet result = Connection.getConnection().excecuteQuery("select max(id) from states");
			result.next();
			this.id = result.getInt(1);
		}
		catch (Exception e) {}
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

	public int getCountryId() {
		return countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	public Country getCountry() {
		return Country.find(countryId);
	}

	public void setCountry(Country country) {
		this.countryId = country.getId();
	}
	
	public static State find(int id){
		State state = new State();
		try { 
			ResultSet result = Connection.getConnection().excecuteQuery("select * from states where id="+id);
			if(result.next()){
				state.id = id;
				state.name = result.getString("name");
			}
			else
				Logger.warning("Couldn't find tag with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search: "+e.toString()); }
		
		return state;
	}
	
	public static List<State> all(){
		List<State> states = new ArrayList<State>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from states");
			while(result.next()) states.add(find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search: "+e.toString()); }
		return states;
	}
	
	@Override
	public boolean save() {
		String sql = "";
		if(id==0) sql = "insert into states(`name`) values('"+name+"')";
		else sql = "update tags set name='"+name+"' where id="+id;
		try { Connection.getConnection().excecuteUpdate(sql); if(id==0) setId(); return true; }
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		return true;
	}
	
	public static void destroyAll(){
		for(State state : all())
			state.destroy();
	}

	@Override
	public String attributes(){
		return "";
	}
	
	@Override
	public String toString() {
		return "";
	}

}
