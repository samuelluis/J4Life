package models;

import java.util.ArrayList;
import java.util.List;

public class City implements IModel {
	
	private int id;
	private String name;
	private String alias;
	
	private City(){
		this(null, null);
	}
	
	public City(String name, String alias){
		this.id = 0;
		this.name = name;
		this.alias = alias;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public static City find(int id){
		City city = new City();
		
		return city;
	}
	
	public static List<City> all(){
		List<City> cities = new ArrayList<City>();
		
		return cities;
	}
	
	@Override
	public boolean save() {
		return true;
	}

	@Override
	public boolean destroy() {
		return true;
	}
	
	public static void destroyAll(){
		for(City city : all())
			city.destroy();
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
