import models.Connection;


public class J4Life {
	public static void main(String[] args) {
		Connection.getConnection();
		
		//TESTING LOGGER
		/*Logger.config("Loggin in file J4Life");
		Logger.config("Loggin in file J4Life.log 2.");
		Logger.fine("Apps Initialized Successfully.");
		Logger.finer("The Logger is working, it means that this class Logger is amazing to do this things");
		Logger.finest("the table products was successfully created in J4Life.db by the next sentence: " +
			"create table products(" +
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
		Logger.info("This App was Developed by Samuel Luis De La Cruz and Joel Abraham Espinal Carrasco.");
		Logger.warning("The Database is at 95% of capacity, please do a clean up of it.");
		try {
			Integer.parseInt("Hola");
		} catch (Exception e) {
			Logger.severe(e.toString());
			Logger.message(e.getMessage());
		}*/
		
		// TESTING TAGS
		/*
		Tag tag = new Tag("Recall");
		System.out.println(tag.getId() + tag.getName());
		System.out.println(tag.attributes());
		System.out.println(tag.destroy());
		*/
		//TESTING COUNTRIES
		//Country country = new Country("Domimican Republic");
		
		Connection.getConnection().close();
	}
}
