package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateHelper;
import utils.Logger;

public class Role implements IModel {
	
	private int id;
	private String name;
	private String alias;
	private Date createdAt;
	private Date updatedAt;
	
	private Role(){
		this.id = 0;
	}
	
	public Role(String name){
		this(name,"");
	}
	
	public Role(String name, String alias){
		this.id = 0;
		this.name = name;
		this.alias = alias;
	}

	public static Role create(String name) {
		return create(name,"");
	}
	
	public static Role create(String name, String alias) {
		Role role = new Role(name,alias);
		return role.save() ? role : null;
	}

	public int getId() {
		return id;
	}

	private void afterSave() {
		try {
			ResultSet result = Connection.getConnection().executeQuery("select max(id) from roles");
			if(result!=null) id = result.getInt(1);
			Role role = Role.find(id);
			this.createdAt = role.createdAt;
			this.updatedAt = role.updatedAt;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	public static Role find(int id){
		Role role = new Role();
		try {
			ResultSet result = Connection.getConnection().executeQuery("select * from roles where id="+id, "Couldn't complete the search of Role: ");
			if(result!=null && result.next()){
				role.id = id;
				role.name = result.getString("name");
				role.alias = result.getString("alias");
				role.createdAt = result.getDate("created_at");
				role.updatedAt = result.getDate("updated_at");
			}
			else
				Logger.warning("Couldn't find Role with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search of Role: "+e.toString()); }
		
		
		return role;
	}
	
	public static List<Role> all(){
		List<Role> roles = new ArrayList<Role>();
		try {
			ResultSet result = Connection.getConnection().executeQuery("select id from roles", "Couldn't complete the search of Roles: ");
			while(result!=null && result.next()) roles.add(find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Roles: "+e.toString()); }
		return roles;
	}
	
	@Override
	public boolean save() {
		String sql = "";
		if(id == 0) sql = "insert into roles(`name`,`alias`,`created_at`,`updated_at`) values('"+name+"','"+alias+"','"+DateHelper.getDateTimeString(new Date())+"','"+DateHelper.getDateTimeString(new Date())+"')";
		else sql = "update roles set name='"+name+"', alias='"+alias+"', updated_at='"+DateHelper.getDateTimeString(new Date())+"' where id="+id;
		try{boolean result = Connection.getConnection().executeUpdate(sql, "Couldn't Save: ".concat(attributes()).concat("\nReason: ")); if(result && id == 0) afterSave(); return result;}
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try {boolean result = Connection.getConnection().executeUpdate("delete from roles where id="+id, "Couldn't Delete: ".concat(attributes()).concat("\nReason: ")); return result;} 
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(Role role : all()) role.destroy();
	}

	@Override
	public String attributes(){
		return "Role [ id="+id+", name='"+name+"', alias='"+alias+"', created_at='"+DateHelper.getDateTimeString(createdAt)+"', updated_at='"+DateHelper.getDateTimeString(updatedAt)+"' ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (!isNew() && id==((Role)obj).id);
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public boolean isNew() {
		return id==0;
	}
}
