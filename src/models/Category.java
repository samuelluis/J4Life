package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateHelper;
import utils.Logger;

public class Category implements IModel {

	private int id;
	private String name;
	private Date createdAt;
	private Date updatedAt;
	
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
	
	private void afterSave() {
		try {
			ResultSet result = Connection.getConnection().executeQuery("select max(id) from categories");
			if(result!=null) id = result.getInt(1);
			Category category = Category.find(id);
			this.createdAt = category.createdAt;
			this.updatedAt = category.updatedAt;
		} catch (SQLException e) {}
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
	public static Category find(int id){
		Category category = new Category();
		try { 
			ResultSet result = Connection.getConnection().executeQuery("select * from categories where id="+id, "Couldn't complete the search of Category: ");
			if(result!=null && result.next()){
				category.id = id;
				category.name = result.getString("name");
				category.createdAt = result.getDate("created_at");
				category.updatedAt = result.getDate("updated_at");
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
			ResultSet result = Connection.getConnection().executeQuery("select id from categories", "Couldn't complete the search of Categories: ");
			while(result!=null && result.next()) categories.add(find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Categories: "+e.toString()); }
		return categories;
	}
	
	public List<Product> getProducts(){
		List<Product> products = new ArrayList<Product>();
		try {
			ResultSet result = Connection.getConnection().executeQuery("select id from products where category_id="+id, "Couldn't complete the search of Products: ");
			while(result!=null && result.next()) products.add(Product.find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Products: "+e.toString()); }
		return products;
	}

	@Override
	public boolean save() {
		String sql = "";
		if(id==0) sql = "insert into categories(`name`,`created_at`,`updated_at`) values('"+name+"','"+DateHelper.getDateTimeString(new Date())+"','"+DateHelper.getDateTimeString(new Date())+"')";
		else sql = "update categories set name='"+name+"', updated_at='"+DateHelper.getDateTimeString(new Date())+"' where id="+id;
		try { boolean result = Connection.getConnection().executeUpdate(sql, "Couldn't Save: ".concat(attributes()).concat("\nReason: ")); if(result && id==0) afterSave(); return result; }
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try { boolean result = Connection.getConnection().executeUpdate("delete from categories where id="+id, "Couldn't Delete: ".concat(attributes()).concat("\nReason: ")); return result; }
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(Category category : Category.all())
			category.destroy();
	}

	@Override
	public String attributes() {
		return "Category [ id="+id+", name='"+name+"', created_at='"+DateHelper.getDateTimeString(createdAt)+"', updated_at='"+DateHelper.getDateTimeString(updatedAt)+"' ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (!isNew() && id==((Category)obj).id);
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
