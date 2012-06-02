package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateHelper;
import utils.Logger;

public class Tag implements IModel {

	private int id;
	private String name;
	private Date createdAt;
	private Date updatedAt;
	
	private Tag() {
		this("");
	}

	public Tag(String name) {
		super();
		this.id = 0;
		this.name = name;
	}

	public static Tag create(String name) {
		Tag tag = new Tag(name);
		return tag.save() ? tag : null;
	}

	public int getId() {
		return id;
	}
	
	private void afterSave() {
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select max(id) from tags");
			id = result.getInt(1);
			Tag tag = Tag.find(id);
			this.createdAt = tag.createdAt;
			this.updatedAt = tag.updatedAt;
		} catch (SQLException e) {}
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean addProduct(Product product){
		return addProduct(product.getId());
	}

	public boolean addProduct(int productId){
		try { Connection.getConnection().excecuteUpdate("insert into products_tags(`tag_id`,`product_id`) values("+id+","+productId+")"); return true; }
		catch (Exception e) { Logger.severe("Couldn't Add Product: ".concat(productId+"").concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public boolean removeProduct(Product product){
		return addProduct(product.getId());
	}

	public boolean removeProduct(int productId){
		try { Connection.getConnection().excecuteUpdate("delete from products_tags where tag_id="+id+" and product_id="+productId); return true; }
		catch (Exception e) { Logger.severe("Couldn't Remove Product: ".concat(productId+"").concat("\nReason: ".concat(e.toString()))); return false;}
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
	
	public static Tag find(int id){
		Tag tag = new Tag();
		try { 
			ResultSet result = Connection.getConnection().excecuteQuery("select * from tags where id="+id);
			if(result.next()){
				tag.id = id;
				tag.name = result.getString("name");
				tag.createdAt = result.getDate("created_at");
				tag.updatedAt = result.getDate("updated_at");
			}
			else
				Logger.warning("Couldn't find Tag with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search of Tag: "+e.toString()); }
		
		return tag;
	}
	
	public static List<Tag> all(){
		List<Tag> tags = new ArrayList<Tag>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from tags");
			while(result.next()) tags.add(find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Tags: "+e.toString()); }
		return tags;
	}
	
	public List<Product> getProducts(){
		List<Product> products = new ArrayList<Product>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select product_id from products_tags where tag_id="+id);
			while(result.next()) products.add(Product.find(result.getInt("product_id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Products: "+e.toString()); }
		return products;
	}

	@Override
	public boolean save() {
		String sql = "";
		if(id==0) sql = "insert into tags(`name`,`created_at`,`updated_at`) values('"+name+"','"+DateHelper.getDateTimeString(new Date())+"','"+DateHelper.getDateTimeString(new Date())+"')";
		else sql = "update tags set name='"+name+"', updated_at='"+DateHelper.getDateTimeString(new Date())+"' where id="+id;
		try { Connection.getConnection().excecuteUpdate(sql); if(id==0) afterSave(); return true; }
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try { Connection.getConnection().excecuteUpdate("delete from tags where id="+id); return true; }
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(Category category : Category.all())
			category.destroy();
	}

	@Override
	public String attributes() {
		return "Tag [ id="+id+", name='"+name+"', created_at='"+DateHelper.getDateTimeString(createdAt)+"', updated_at='"+DateHelper.getDateTimeString(updatedAt)+"' ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (!isNew() && id==((Tag)obj).id);
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
