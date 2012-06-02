package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateHelper;
import utils.Logger;

public class Address implements IModel {

	private int id;
	private String street;
	private String number;
	private String zipcode;
	private int cityId;
	private Date createdAt;
	private Date updatedAt;
	
	private Address(){
		this.id = 0;
	}
	
	public Address(String street, String zipcode, City city){
		this(street,zipcode,"",city);
	}
	
	public Address(String street, String zipcode, int cityId){
		this(street,zipcode,"",cityId);
	}
	

	public Address(String street, String zipcode, String number, City city){
		this(street,zipcode,number,city.getId());
	}
	
	public Address(String street, String zipcode, String number, int cityId){
		this.id = 0;
		this.street = street;
		this.number = number;
		this.zipcode = zipcode;
		this.cityId = cityId;
	}

	public static Address create(String street, String zipcode, City city) {
		return create(street,zipcode,"",city);
	}
	
	public static Address create(String street, String zipcode, int cityId) {
		return create(street,zipcode,"",cityId);
	}
	
	public static Address create(String street, String zipcode, String number, City city) {
		return create(street,zipcode,number,city.getId());
	}
	
	public static Address create(String street, String zipcode, String number, int cityId) {
		Address address = new Address(street,zipcode,number,cityId);
		return address.save() ? address : null;
	}

	public int getId() {
		return id;
	}

	private void afterSave() {
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select max(id) from addresses");
			id = result.getInt(1);
			Address address = Address.find(id);
			this.createdAt = address.createdAt;
			this.updatedAt = address.updatedAt;
		} catch (SQLException e) {}
	}
	
	@Override
	public String getName() {
		return street+", "+number+", "+getCity().getName()+", "+getCity().getState().getName()+", "+getCity().getState().getCountry().getName();
	}
	
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public int getCityId(){
		return cityId;
	}
	
	public void setCityId(int cityId){
		this.cityId = cityId;
	}
	
	public City getCity(){
		return City.find(cityId);
	}
	
	public void setCity(City city){
		cityId = city.getId();
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

	public static Address find(int id){
		Address address = new Address();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select * from addresses where id = " + id);
			if(result.next()){
				address.id = id;
				address.street = result.getString("street");
				address.number = result.getString("number");
				address.zipcode = result.getString("zipcode");
				address.cityId = result.getInt("city_id");
				address.createdAt = result.getDate("created_at");
				address.updatedAt = result.getDate("updated_at");
			}
			else
				Logger.warning("Couldn't find Address with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search of Address: "+e.toString()); }
		
		
		return address;
	}
	
	public static List<Address> all(){
		List<Address> addresses = new ArrayList<Address>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from addresses");
			while(result.next()) addresses.add(find(result.getInt("id")));
			
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Addresses: "+e.toString()); }
		return addresses;
	}
	
	@Override
	public boolean save() {
		String sql = "";
		if(id == 0) sql = "insert into addresses(`street`,`number`,`zipcode`,`city_id`,`created_at`,`updated_at`) values('"+street+"','"+number+"','"+zipcode+"',"+cityId+",'"+DateHelper.getDateTimeString(new Date())+"','"+DateHelper.getDateTimeString(new Date())+"')";
		else sql = "update addresses set street='"+street+"', number='"+number+"', zipcode='"+zipcode+"', city_id="+cityId+", updated_at='"+DateHelper.getDateTimeString(new Date())+"' where id="+id;
		try{Connection.getConnection().excecuteUpdate(sql); if(id == 0) afterSave(); return true;}
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try {Connection.getConnection().excecuteUpdate("delete from addresses where id="+id); return true;} 
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(Address address : all()) address.destroy();
	}

	@Override
	public String attributes(){
		return "Address [ id="+id+", street='"+street+"', number'"+number+"', zipcode='"+zipcode+"', city_id="+cityId+", " +
				"created_at="+DateHelper.getDateTimeString(createdAt)+", updated_at="+DateHelper.getDateTimeString(updatedAt)+" ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (!isNew() && id==((Address)obj).id);
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
