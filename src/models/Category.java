package models;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utils.Logger;

public class Category implements IModel {

	private int id;
	private String name;
	
	private Category() {
		this("");
	}

	public Category(String name) {
		super();
		this.id = 0;
		this.name = name;
	}

	public static Category create(String name) {
		Category category = new Category(name);
		return category.save() ? category : null;
	}

	public int getId() {
		return id;
	}

	public void setId() {
		try { 
			ResultSet result = Connection.getConnection().excecuteQuery("select max(id) from categories");
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
	
	public static Category find(int id){
		Category category = new Category();
		try { 
			ResultSet result = Connection.getConnection().excecuteQuery("select * from categories where id="+id);
			if(result.next()){
				category.id = id;
				category.name = result.getString("name");
			}
			else
				Logger.warning("Couldn't find Category with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search of Category: "+e.toString()); }
		
		return category;
	}
	
	public static List<Category> all(){
		List<Category> categories = new ArrayList<Category>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from categories");
			while(result.next()) categories.add(find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Categories: "+e.toString()); }
		return categories;
	}

	@Override
	public boolean save() {
		String sql = "";
		if(id==0) sql = "insert into categories(`name`) values('"+name+"')";
		else sql = "update categories set name='"+name+"' where id="+id;
		try { Connection.getConnection().excecuteUpdate(sql); if(id==0) setId(); return true; }
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try { Connection.getConnection().excecuteUpdate("delete from categories where id="+id); return true; }
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(Category category : Category.all())
			category.destroy();
	}

	@Override
	public String attributes() {
		return "Category [ id="+id+", name='"+name+"' ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
