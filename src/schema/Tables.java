package schema;

import java.util.ArrayList;

public class Tables extends ArrayList<String>{

	public Tables(){
		add("create table countries(" +
			"id integer primary key autoincrement," +
			"name varchar(30) unique," +
			"alias varchar(5)" +
		")");
		add("create table states(" +
			"id integer primary key autoincrement," +
			"name varchar(30) unique," +
			"alias varchar(5)," +
			"country_id intenger references countries(id)" +
		")");
		add("create table cities(" +
			"id integer primary key autoincrement," +
			"name varchar(30) unique," +
			"alias varchar(5)," +
			"state_id integer references states(id)" +
		")");
		add("create table categories(" +
			"id integer primary key autoincrement," +
			"name varchar(30) unique," +
			"alias varchar(5)" +
		")");
		add("create table products(" +
			"id integer primary key autoincrement," +
			"name varchar(100)," +
			"unit varchar(15)," +
			"quantity integer," +
			"price numeric(11,2)," +
			"unit_price numeric(11,2)," +
			"lp integer" +
			"type varchar(30)," +
			"category_id integer references categories(id)" +
		")");
		add("create table tags(" +
			"id integer primary key autoincrement," +
			"name varchar(50)," +
			"alias varchar(5)" +
		")");
		add("create table products_tags(" +
			"product_id integer references products(id)," +
			"tag_id integer references tags(id)," +
			"primary key (product_id, tag_id)" +
		")");
		add("create table address(" +
			"id integer primary key autoincrement,"+
			"street varchar(50)," +
			"number integer," +
			"city_id integer references cities(id)," +
			"zip_code varchar(10)" +
		")");
		add("create table people(" +
			"id integer primary key autoincrement," +
			"name varchar(50)," +
			"last_name varchar(50)," +
			"identifier varchar(15)," +
			"dob date," +
			"phone varchar(15)," +
			"cel_phone varchar(15)" +
		")");
		add("create table users(" +
			"id integer primary key autoincrement," +
			"email varchar(100)," +
			"user_name varchar(50)," +
			"password varchar(50)," +
			"person_id integer references people(id)" +
		")");
		add("create table roles(" +
			"id integer primary key autoincrement," +
			"name varchar(50)," +
			"alias varchar(50)" +
		")");
		add("create table roles_users(" +
			"user_id integer references users(id)," +
			"role_id integer references roles(id)," +
			"primary key(user_id, role_id)" +
		")");
		add("create table branches(" +
			"id integer primary key autoincrement," +
			"address_id integer references address(id)," +
			"person_id integer references people(id)" +
		")");
		add("create table products_branches(" +
			"product_id integer references products(id)," +
			"branch_id integer references branches(id)," +
			"unit integer," +
			"primary key(product_id, branch_id)" +
		")");
		add("create table branches_users(" +
			"branch_id integer references branches," +
			"user_id integer references users," +
			"primary key(branch_id,user_id)" +
		")");
		add("create table clients(" +
			"id integer primary key," +
			"person_id integer references people(id)" +
		")");
		add("create table partners(" +
			"id integer primary key autoincrement," +
			"code varchar(50)" +
		")");
		add("create table bills(" +
			"id integer primary key autoincrement," +
			"efective_date date," +
			"user_id integer references users(id)," +
			"client_id integer references clients(id)," +
			"partner_id integer references partners(id)," +
			"branch_id integer references branches(id)," +
			"units integer" +
		")");
		add("create table bills_products(" +
			"product_id integer references products(id)," +
			"bill_id integer references bills(id)," +
			"quantity integer," +
			"primary key(product_id,bill_id)" +
		")");
	}
}
