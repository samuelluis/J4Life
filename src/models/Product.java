package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateHelper;
import utils.Logger;

public class Product implements IModel {

	private int id;
	private String name;
	private String unit;
	private int quantity;
	private double price;
	private double unitPrice;
	private int lp;
	private Type type;
	private int categoryId;
	private Date createdAt;
	private Date updatedAt;
	
	private Product(){
		this.id = 0;
	}
	
	public Product(String name){
		this(name,"",1,0.0,0.0,0,null,0);
	}
	
	public Product(String name, String unit, int quantity, double price, double unitPrice, int lp, Type type, Category category) {
		this(name, unit, quantity, price, unitPrice, lp, type, category.getId());
	}
	
	public Product(String name, String unit, int quantity, double price, double unitPrice, int lp, Type type, int categoryId) {
		this.id = 0;
		this.name = name;
		this.unit = unit;
		this.quantity = quantity;
		this.price = price;
		this.unitPrice = unitPrice;
		this.lp = lp;
		this.type = type;
		this.categoryId = categoryId;
	}
	
	public static Product create(String name){
		return create(name,"",1,0.0,0.0,0,null,0);
	}
	
	public static Product create(String name, String unit, int quantity, double price, double unitPrice, int lp, Type type, Category category) {
		return create(name, unit, quantity, price, unitPrice, lp, type, category.getId());
	}
	
	public static Product create(String name, String unit, int quantity, double price, double unitPrice, int lp, Type type, int categoryId) {
		Product product = new Product(name, unit, quantity, price, unitPrice, lp, type, categoryId);
		return product.save() ? product : null; 
	}

	public int getId() {
		return id;
	}

	private void afterSave() {
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select max(id) from products");
			id = result.getInt(1);
			Product product = Product.find(id);
			this.createdAt = product.createdAt;
			this.updatedAt = product.updatedAt;
		} catch (SQLException e) {}
	}
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public int getLp() {
		return lp;
	}

	public void setLp(int lp) {
		this.lp = lp;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getCategoryId(){
		return categoryId;
	}
	
	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}
	
	public Category getCategory(){
		return Category.find(categoryId);
	}
	
	public void setCategory(Category category){
		categoryId = category.getId();
	}
	
	public boolean addTag(Tag tag){
		return addTag(tag.getId());
	}

	public boolean addTag(int tagId){
		try { Connection.getConnection().excecuteUpdate("insert into products_tags(`product_id`,`tag_id`) values("+id+","+tagId+")"); return true; }
		catch (Exception e) { Logger.severe("Couldn't Add Tag: ".concat(tagId+"").concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public boolean removeTag(Tag tag){
		return removeTag(tag.getId());
	}

	public boolean removeTag(int tagId){
		try { Connection.getConnection().excecuteUpdate("delete from products_tags where product_id="+id+" and tag_id="+tagId); return true; }
		catch (Exception e) { Logger.severe("Couldn't Remove Tag: ".concat(tagId+"").concat("\nReason: ".concat(e.toString()))); return false;}
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

	public static Product find(int id){
		Product product = new Product();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select * from products where id = " + id);
			if(result.next()){
				product.id = id;
				product.name = result.getString("name");
				product.unit = result.getString("unit");
				product.quantity = result.getInt("quantity");
				product.price = result.getDouble("price");
				product.unitPrice = result.getDouble("unit_price");
				product.lp = result.getInt("lp");
				product.type = Type.valueOf(result.getString("type"));
				product.categoryId = result.getInt("category_id");
				product.createdAt = result.getDate("created_at");
				product.updatedAt = result.getDate("updated_at");
			}
			else
				Logger.warning("Couldn't find Product with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search of Product: "+e.toString()); }
		
		
		return product;
	}
	
	public static List<Product> all(){
		List<Product> products = new ArrayList<Product>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from products");
			while(result.next()) products.add(find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Products: "+e.toString()); }
		return products;
	}
	
	public List<Tag> getProducts(){
		List<Tag> tags = new ArrayList<Tag>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select tag_id from products_tags where product_id="+id);
			while(result.next()) tags.add(Tag.find(result.getInt("tag_id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Tags: "+e.toString()); }
		return tags;
	}
	
	@Override
	public boolean save() {
		String sql = "";
		if(id == 0) sql = "insert into products(`name`,`unit`,`quantity`,`price`,`unit_price`,`lp`,`type`,`category_id`,`created_at`,`updated_at`) values('"+name+"','"+unit+"',"+quantity+","+price+","+unitPrice+","+lp+",'"+type+"',"+categoryId+",'"+DateHelper.getDateTimeString(new Date())+"','"+DateHelper.getDateTimeString(new Date())+"')";
		else sql = "update products set name='"+name+"', unit='"+unit+"', quantity="+quantity+", price="+price+", unit_price="+unitPrice+", lp="+lp+", type='"+type+"', category_id="+categoryId+", updated_at='"+DateHelper.getDateTimeString(new Date())+"' where id="+id;
		try{Connection.getConnection().excecuteUpdate(sql); if(id == 0) afterSave(); return true;}
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try {Connection.getConnection().excecuteUpdate("delete from products where id="+id); return true;} 
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(Product product : all()) product.destroy();
	}

	@Override
	public String attributes(){
		return "Product [ id="+id+", name='"+name+"', unit='"+unit+"', quantity="+quantity+", price="+price+", unit_price="+unitPrice+", lp="+lp+", type='"+type+"', category_id="+categoryId+", " +
				"created_at="+DateHelper.getDateTimeString(createdAt)+", updated_at="+DateHelper.getDateTimeString(updatedAt)+" ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (!isNew() && id==((Product)obj).id);
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
