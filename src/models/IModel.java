package models;

public interface IModel {
	public String getName();
	public boolean save();
	public boolean destroy();
	public String attributes();
}
