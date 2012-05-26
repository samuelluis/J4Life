package models;

import java.util.ArrayList;
import java.util.List;

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
		this.id = 0;
		this.name = name;
		this.alias = alias;
		this.countryId = countryId;
	}

	public int getId() {
		return id;
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
		
		return state;
	}
	
	public static List<State> all(){
		List<State> states = new ArrayList<State>();
		
		return states;
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
