package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateHelper;
import utils.Logger;

public class Branch implements IModel {
	
	private int id;
	private String name;
	private int addressId;
	private int personId;
	private Date createdAt;
	private Date updatedAt;
	
	private Branch(){
		this.id = 0;
	}
	
	public Branch(String name, Address address, Person person){
		this(name, address.getId(), person.getId());
	}
	
	public Branch(String name, int addressId, int personId){
		this.id = 0;
		this.name = name;
		this.addressId = addressId;
		this.personId = personId;
	}
	
	public static Branch create(String name, Address address, Person person) {
		return create(name, address.getId(), person.getId());
	}
	
	public static Branch create(String name, int addressId, int personId) {
		Branch branch = new Branch(name, addressId,personId);
		return branch.save() ? branch : null;
	}

	public int getId() {
		return id;
	}

	private void afterSave() {
		try {
			ResultSet result = Connection.getConnection().executeQuery("select max(id) from branches");
			if(result!=null) id = result.getInt(1);
			Branch branch = Branch.find(id);
			this.createdAt = branch.createdAt;
			this.updatedAt = branch.updatedAt;
		} catch (SQLException e) {}
	}
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return Address.find(addressId);
	}

	public void setAddress(Address address) {
		this.addressId = address.getId();
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public Person getPerson() {
		return Person.find(personId);
	}

	public void setPerson(Person person) {
		this.personId = person.getId();
	}

	public int getPersonId() {
		return personId;
	}

	public void setPersonId(int personId) {
		this.personId = personId;
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
	
	public static Branch find(int id){
		Branch country = new Branch();
		try {
			ResultSet result = Connection.getConnection().executeQuery("select * from branches where id="+id, "Couldn't complete the search of Branch: ");
			if(result!=null && result.next()){
				country.id = id;
				country.name = result.getString("name");
				country.addressId = result.getInt("address_id");
				country.personId = result.getInt("person_id");
				country.createdAt = result.getDate("created_at");
				country.updatedAt = result.getDate("updated_at");
			}
			else
				Logger.warning("Couldn't find Branch with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search of Branch: "+e.toString()); }
		
		
		return country;
	}
	
	public static List<Branch> all(){
		List<Branch> branches = new ArrayList<Branch>();
		try {
			ResultSet result = Connection.getConnection().executeQuery("select id from branches", "Couldn't complete the search of Branches: ");
			while(result!=null && result.next()) branches.add(find(result.getInt("id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Branches: "+e.toString()); }
		return branches;
	}
	
	@Override
	public boolean save() {
		String sql = "";
		if(id == 0) sql = "insert into branches(`name`,`address_id`,`person_id`,`created_at`,`updated_at`) values('"+name+"',"+addressId+","+personId+",'"+DateHelper.getDateTimeString(new Date())+"','"+DateHelper.getDateTimeString(new Date())+"')";
		else sql = "update branches set name='"+name+"', address_id="+addressId+", person_id="+personId+", updated_at='"+DateHelper.getDateTimeString(new Date())+"' where id="+id;
		try{boolean result = Connection.getConnection().executeUpdate(sql, "Couldn't Save: ".concat(attributes()).concat("\nReason: ")); if(result && id == 0) afterSave(); return result;}
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try {boolean result = Connection.getConnection().executeUpdate("delete from branches where id="+id, "Couldn't Delete: ".concat(attributes()).concat("\nReason: ")); return result;} 
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(Branch branch : all()) branch.destroy();
	}

	@Override
	public String attributes(){
		return "Country [ id="+id+", name='"+name+"', address_id="+addressId+", person_id="+personId+", created_at='"+DateHelper.getDateTimeString(createdAt)+"', updated_at='"+DateHelper.getDateTimeString(updatedAt)+"' ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (!isNew() && id==((Branch)obj).id);
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
