package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import utils.DateHelper;
import utils.Logger;
import utils.PasswordHelper;

public class User implements IModel {

	private int id;
	private String email;
	private String username;
	private String password;
	private int personId;
	private Date createdAt;
	private Date updatedAt;
	
	private User(){
		this.id = 0;
	}
	
	public User(String email, String password, Person person){
		this(email,password,"",person);
	}
	
	public User(String email, String password, int personId){
		this(email,password,"",personId);
	}
	

	public User(String email, String password, String username, Person person){
		this(email,password,username,person.getId());
	}
	
	public User(String email, String password, String username, int personId){
		this.id = 0;
		this.email = email;
		this.username = username;
		this.password = PasswordHelper.toMD5(password);
		this.personId = personId;
	}

	public static User create(String email, String password, Person person) {
		return create(email,password,"",person);
	}
	
	public static User create(String email, String password, int personId) {
		return create(email,password,"",personId);
	}
	
	public static User create(String email, String password, String username, Person person) {
		return create(email,password,username,person.getId());
	}
	
	public static User create(String email, String password, String username, int personId) {
		User user = new User(email,password,username,personId);
		return user.save() ? user : null;
	}

	public int getId() {
		return id;
	}

	private void afterSave() {
		ResultSet result = Connection.getConnection().executeQuery("select max(id) from users");
		try {if(result !=null) id = result.getInt(1);} catch (SQLException e) {}
		User user = User.find(id);
		this.createdAt = user.createdAt;
		this.updatedAt = user.updatedAt;
	}
	
	@Override
	public String getName() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = PasswordHelper.toMD5(password);
	}
	
	public int getPersonId(){
		return personId;
	}
	
	public void setPersonId(int personId){
		this.personId = personId;
	}
	
	public Person getPerson(){
		return Person.find(personId);
	}
	
	public void setPerson(Person person){
		personId = person.getId();
	}
	
	public boolean addRole(Role role){
		return addRole(role.getId());
	}

	public boolean addRole(int roleId){
		try { boolean result = Connection.getConnection().executeUpdate("insert into roles_users(`user_id`,`role_id`) values("+id+","+roleId+")", "Couldn't Add Role: ".concat(roleId+"").concat("\nReason: ")); return result; }
		catch (Exception e) { Logger.severe("Couldn't Add Role: ".concat(roleId+"").concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public boolean removeRole(Role role){
		return removeRole(role.getId());
	}

	public boolean removeRole(int roleId){
		try {boolean result = Connection.getConnection().executeUpdate("delete from roles_users where user_id="+id+" and role_id="+roleId, "Couldn't Remove Role: ".concat(roleId+"").concat("\nReason: ")); return result; }
		catch (Exception e) { Logger.severe("Couldn't Remove Role: ".concat(roleId+"").concat("\nReason: ".concat(e.toString()))); return false;}
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

	public static User find(int id){
		User user = new User();
		try {
			ResultSet result = Connection.getConnection().executeQuery("select * from users where id = " + id, "Couldn't complete the search of User: ");
			if(result!=null && result.next()){
				user.id = id;
				user.email = result.getString("email");
				user.username = result.getString("username");
				user.password = result.getString("password");
				user.personId = result.getInt("person_id");
				user.createdAt = result.getDate("created_at");
				user.updatedAt = result.getDate("updated_at");
			}
			else
				Logger.warning("Couldn't find User with id: "+id);
		}
		catch (Exception e) { Logger.severe("Couldn't complete the search of User: "+e.toString()); }
		
		
		return user;
	}
	
	public static List<User> all(){
		List<User> users = new ArrayList<User>();
		try {
			ResultSet result = Connection.getConnection().executeQuery("select id from users", "Couldn't complete the search of Users: ");
			while(result!=null && result.next()) users.add(find(result.getInt("id")));
			
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Users: "+e.toString()); }
		return users;
	}
	
	public List<Role> getRoles(){
		List<Role> roles = new ArrayList<Role>();
		try {
			ResultSet result = Connection.getConnection().executeQuery("select role_id from roles_users where user_id="+id, "Couldn't complete the search of Roles: ");
			while(result!=null && result.next()) roles.add(Role.find(result.getInt("role_id")));
		} catch (Exception e) { Logger.severe("Couldn't complete the search of Roles: "+e.toString()); }
		return roles;
	}
	
	@Override
	public boolean save() {
		String sql = "";
		if(id == 0) sql = "insert into users(`email`,`username`,`password`,`person_id`,`created_at`,`updated_at`) values('"+email+"','"+username+"','"+password+"',"+personId+",'"+DateHelper.getDateTimeString(new Date())+"','"+DateHelper.getDateTimeString(new Date())+"')";
		else sql = "update users set email='"+email+"', username='"+username+"', password='"+password+"', person_id="+personId+", updated_at='"+DateHelper.getDateTimeString(new Date())+"' where id="+id;
		try{boolean result = Connection.getConnection().executeUpdate(sql, "Couldn't Save: ".concat(attributes()).concat("\nReason: ")); if(result && id == 0) afterSave(); return result;}
		catch (Exception e) { Logger.severe("Couldn't Save: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}

	@Override
	public boolean destroy() {
		try {boolean result = Connection.getConnection().executeUpdate("delete from users where id="+id, "Couldn't Delete: ".concat(attributes()).concat("\nReason: ")); return result;} 
		catch (Exception e) { Logger.severe("Couldn't Delete: ".concat(attributes()).concat("\nReason: ".concat(e.toString()))); return false;}
	}
	
	public static void destroyAll(){
		for(User user : all()) user.destroy();
	}

	@Override
	public String attributes(){
		return "User [ id="+id+", email='"+email+"', username'"+username+"', password='"+password+"', person_id="+personId+", " +
				"created_at="+DateHelper.getDateTimeString(createdAt)+", updated_at="+DateHelper.getDateTimeString(updatedAt)+" ]";
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (!isNew() && id==((User)obj).id);
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
