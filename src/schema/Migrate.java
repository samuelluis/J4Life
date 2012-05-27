package schema;

import java.util.HashMap;

@SuppressWarnings("serial")
public class Migrate extends HashMap<String, String>{

	public Migrate(){
		put("countries","create table countries(" +
			"id integer primary key autoincrement," +
			"name varchar(30) unique," +
			"alias varchar(5)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("states","create table states(" +
			"id integer primary key autoincrement," +
			"name varchar(30) unique," +
			"alias varchar(5)," +
			"country_id intenger references countries(id)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("cities","create table cities(" +
			"id integer primary key autoincrement," +
			"name varchar(30) unique," +
			"alias varchar(5)," +
			"state_id integer references states(id)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("categories","create table categories(" +
			"id integer primary key autoincrement," +
			"name varchar(30) unique," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("products","create table products(" +
			"id integer primary key autoincrement," +
			"name varchar(100)," +
			"unit varchar(15)," +
			"quantity integer," +
			"price numeric(11,2)," +
			"unit_price numeric(11,2)," +
			"lp integer" +
			"type varchar(30)," +
			"category_id integer references categories(id)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("tags","create table tags(" +
			"id integer primary key autoincrement," +
			"name varchar(50)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("products_tags","create table products_tags(" +
			"product_id integer references products(id)," +
			"tag_id integer references tags(id)," +
			"created_at datetime," +
			"updated_at datetime," +
			"primary key (product_id, tag_id)" +
		")");
		put("address","create table address(" +
			"id integer primary key autoincrement,"+
			"street varchar(50)," +
			"number integer," +
			"city_id integer references cities(id)," +
			"zip_code varchar(10)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("people","create table people(" +
			"id integer primary key autoincrement," +
			"name varchar(50)," +
			"last_name varchar(50)," +
			"identifier varchar(15)," +
			"dob date," +
			"phone varchar(15)," +
			"cel_phone varchar(15)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("users","create table users(" +
			"id integer primary key autoincrement," +
			"email varchar(100)," +
			"user_name varchar(50)," +
			"password varchar(50)," +
			"person_id integer references people(id)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("roles","create table roles(" +
			"id integer primary key autoincrement," +
			"name varchar(50)," +
			"alias varchar(50)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("roles_users","create table roles_users(" +
			"user_id integer references users(id)," +
			"role_id integer references roles(id)," +
			"created_at datetime," +
			"updated_at datetime," +
			"primary key(user_id, role_id)" +
		")");
		put("branches","create table branches(" +
			"id integer primary key autoincrement," +
			"address_id integer references address(id)," +
			"person_id integer references people(id)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("products_branches","create table products_branches(" +
			"product_id integer references products(id)," +
			"branch_id integer references branches(id)," +
			"units integer," +
			"created_at datetime," +
			"updated_at datetime," +
			"primary key(product_id, branch_id)" +
		")");
		put("branches_users","create table branches_users(" +
			"branch_id integer references branches," +
			"user_id integer references users," +
			"created_at datetime," +
			"updated_at datetime," +
			"primary key(branch_id,user_id)" +
		")");
		put("clients","create table clients(" +
			"id integer primary key," +
			"person_id integer references people(id)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("partners","create table partners(" +
			"id integer primary key autoincrement," +
			"code varchar(50)," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("bills","create table bills(" +
			"id integer primary key autoincrement," +
			"efective_date date," +
			"user_id integer references users(id)," +
			"client_id integer references clients(id)," +
			"partner_id integer references partners(id)," +
			"branch_id integer references branches(id)," +
			"units integer," +
			"created_at datetime," +
			"updated_at datetime" +
		")");
		put("bills_products","create table bills_products(" +
			"product_id integer references products(id)," +
			"bill_id integer references bills(id)," +
			"quantity integer," +
			"created_at datetime," +
			"updated_at datetime," +
			"primary key(product_id,bill_id)" +
		")");
	}
}
