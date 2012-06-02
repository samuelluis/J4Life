package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateHelper;
import utils.Logger;

public class Person implements IModel {

	private int id;
	private String name;
	private String lastName;
	private String identifier;
	private Date dob;
	private String phone;
	private String celPhone;
	private Date createdAt;
	private Date updatedAt;
	
	private Person(){
		this.id = 0;
	}
	
	public Person(String name, String lastName, String identifier, Date dob){
		this(name,lastName,identifier,dob,"","");
	}
	
	public Person(String name, String lastName, String identifier, Date dob, String phone, String celPhone){
		this.id = 0;
		this.name = name;
		this.lastName = lastName;
		this.identifier = identifier;
		this.phone = phone;
		this.celPhone = celPhone;
		this.dob = dob;
	}

	public static Person create(String name, String lastName, String identifier, Date dob) {
		return create(name,lastName,identifier,dob,"","");
	}
	
	public static Person create(String name, String lastName, String identifier, Date dob, String phone, String celPhone) {
		Person person = new Person(name,lastName,identifier,dob,phone,celPhone);
		return person.save() ? person : null;
	}

	public int getId() {
		return id;
	}

	private void afterSave() {
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select max(id) from people");
			id = result.getInt(1);
			Person person = Person.find(id);
			this.createdAt = person.createdAt;
			this.updatedAt = person.updatedAt;
		} catch (SQLException e) {}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public Date getDOB(){
		return dob;
	}
	
	public void setDOB(Date dob){
		this.dob = dob;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCelPhone() {
		return celPhone;
	}

	public void setCelPhone(String celPhone) {
		this.celPhone = celPhone;
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

	public static Person find(int id){
		Person person = new Person();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select * from people where id = " + id);
			if(result.next()){
				person.id = id;
				person.name = result.getString("name");
				person.lastName = result.getString("last_name");
				person.identifier = result.getString("identifier");
				person.dob = result.getDate("dob");
				person.phone = result.getString("phone");
				person.celPhone = result.getString("cel_phone");
				person.createdAt = result.getDate("created_at");
				person.updatedAt = result.getDate("updated_at");
			}
			else
				Logger.warning("Couldn't find Person with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search of Person: "+e.toString()); }
		
		
		return person;
	}
	
	public static List<Person> all(){
		List<Person> people = new ArrayList<Person>();
		try {
			ResultSet result = Connection.getConnection().excecuteQuery("select id from people");
			while(result.next()) people.add(find(result.getInt("id")));
			
		} catch (Exception e) { Logger.severe("Couldn't complete the search of People: "+e.toString()); }
		return people;
	}
	
	@Override
	public boolean save() {
		String sql = "";
		if(id == 0) sql = "insert into people(`name`,`last_name`,`identifier`,`dob`,`phone`,`cel_phone`,`created_at`,`updated_at`) values('"+name+"','"+lastName+"','"+identifier+"',"+dob+",'"+phone+"','"+celPhone+"','"+DateHelper.getDateTimeString(new Date())+"','"+DateHelper.getDateTimeString(new Date())+"')";
		else sql = "update people set name='"+name+"', last_name='"+lastName+"', identifier='"+identifier+"', dob="+dob+", phone='"+phone+"', cel_phone='"+celPhone+"', updated_at='"+DateHelper.getDateTimeString(new Date())+"' where id="+id;
		try{Connection.getConnection().excecuteUpdate(sql); if(id == 0) afterSave(); return true;}
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try {Connection.getConnection().excecuteUpdate("delete from people where id="+id); return true;} 
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(Person person : all()) person.destroy();
	}

	@Override
	public String attributes(){
		return "Person [ id="+id+", name='"+name+"', last_name'"+lastName+"', identifier='"+identifier+"', dob="+dob+", phone='"+phone+"', cel_phone='"+celPhone+"', " +
				"created_at="+DateHelper.getDateTimeString(createdAt)+", updated_at="+DateHelper.getDateTimeString(updatedAt)+" ]";
	}
	
	@Override
	public String toString() {
		return getName()+" "+getLastName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (!isNew() && id==((Person)obj).id);
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
