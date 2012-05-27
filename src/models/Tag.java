package models;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import utils.Logger;

public class Tag implements IModel {

	private int id;
	private String name;
	
	public Tag() {
		this(null);
	}

	public Tag(String name) {
		super();
		this.id = 0;
		this.name = name;
	}
	
	public static Tag create() {
		return create(null);
	}

	public static Tag create(String name) {
		Tag tag = new Tag(name);
		return tag.save() ? tag : null;
	}

	public int getId() {
		return id;
	}

	public void setId() {
		try { 
			ResultSet result = Connection.getConnection().excecuteQuery("select max(id) from tags");
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
	
	public static Tag find(int id){
		Tag tag = new Tag();
		try { 
			ResultSet result = Connection.getConnection().excecuteQuery("select * from tags where id="+id);
			if(result.next()){
				tag.id = id;
				tag.name = result.getString("name");
			}
			else
				Logger.warning("Couldn't find tag with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search: "+e.toString()); }
		
		return tag;
	}
	
	public static List<Tag> all(){
		List<Tag> tags = new ArrayList<Tag>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from tags");
			while(result.next()) tags.add(find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search: "+e.toString()); }
		return tags;
	}

	@Override
	public boolean save() {
		String sql = "";
		if(id==0) sql = "insert into tags(`name`) values('"+name+"')";
		else sql = "update tags set name='"+name+"' where id="+id;
		try { Connection.getConnection().excecuteUpdate(sql); if(id==0) setId(); return true; }
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try { Connection.getConnection().excecuteUpdate("delete from tags where id="+id); return true; }
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(Tag tag : Tag.all())
			tag.destroy();
	}

	@Override
	public String attributes() {
		return "tags [ id="+id+", name="+name+" ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
