package models;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utils.Logger;

public class Country implements IModel {
// falta el alias
	private int id;
	private String name;
	
	public Country() {
		this(null);
	}

	public Country(String name) {
		super();
		this.id = 0;
		this.name = name;
	}
	
	public static Country create() {
		return create(null);
	}

	public static Country create(String name) {
		Country country = new Country(name);
		return country.save() ? country : null;
	}

	public int getId() {
		return id;
	}

	public void setId() {
		try { 
			ResultSet result = Connection.getConnection().excecuteQuery("select max(id) from countries");
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
	
	public static Country find(int id){
		Country country = new Country();
		try { 
			ResultSet result = Connection.getConnection().excecuteQuery("select * from countries where id="+id);
			if(result.next()){
				country.id = id;
				country.name = result.getString("name");
			}
			else
				Logger.warning("Couldn't find tag with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search: "+e.toString()); }
		
		return country;
	}
	
	public static List<Country> all(){
		List<Country> countries = new ArrayList<Country>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from countries");
			while(result.next()) countries.add(find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search: "+e.toString()); }
		return countries;
	}

	@Override
	public boolean save() {
		String sql = "";
		if(id==0) sql = "insert into countries(`name`) values('"+name+"')";
		else sql = "update tags set name='"+name+"' where id="+id;
		try { Connection.getConnection().excecuteUpdate(sql); if(id==0) setId(); return true; }
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try { Connection.getConnection().excecuteUpdate("delete from countries where id="+id); return true; }
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(Country country : Country.all())
			country.destroy();
	}

	@Override
	public String attributes() {
		return "countries [ id="+id+", name="+name+" ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	public List <State> getStates(){
		List<State> states = new ArrayList<State>();
		return states;
	}
}
