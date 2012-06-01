package models;

public enum Type {
	
	LIQUID("Liquid");
	
	private String name;
	
	Type(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
