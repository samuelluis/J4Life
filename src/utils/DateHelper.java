package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	public static String getDateString(Date date){
		return dateFormat.format(date);
	}
	public static String getDateTimeString(Date date){
		return dateTimeFormat.format(date);
	}

	public static Date getStringDate(String date){
		try {return dateFormat.parse(date);}
		catch (ParseException e) {Logger.severe("Couldn't convert to date the given string: "+date+"\nReason: "+e.toString()); return null;}
	}
	public static Date getStringDateTime(String date){
		try {return dateTimeFormat.parse(date);}
		catch (ParseException e) {Logger.severe("Couldn't convert to date time the given string: "+date+"\nReason: "+e.toString()); return null;}
	}
}
