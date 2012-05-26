package models;

import java.util.ArrayList;
import java.util.List;

public class Country implements IModel {
	
	private int id;
	private String name;
	private String alias;
	
	private Country(){
		this(null, null);
	}
	
	public Country(String name, String alias){
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

	public static Country find(int id){
		Country country = new Country();
		
		return country;
	}
	
	public static List<Country> all(){
		List<Country> countries = new ArrayList<Country>();
		
		return countries;
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
		for(Country country : all())
			country.destroy();
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
