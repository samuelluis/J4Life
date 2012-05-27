package utils;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class Logger {
	
	private static java.util.logging.Logger logger;
	
	private static java.util.logging.Logger getLogger(Level level){
		logger = java.util.logging.Logger.getLogger(capitalize(level.toString()).concat(" Log"));
		logger.setLevel(level);
		if(logger.getHandlers().length==0)
			try {
				FileHandler handler = new FileHandler("logs/".concat(capitalize(level.toString())).concat(".log"),true);
				handler.setFormatter(new SimpleFormatter());
				logger.addHandler(handler);
			} catch (Exception e) {e.printStackTrace();}
		return logger;
	}
	
	private static String capitalize(String str){
		return str.substring(0, 1).toUpperCase().concat(str.substring(1).toLowerCase());
	}
	
	public static void info(String msg){
		getLogger(Level.INFO).info(msg);
		message(msg);
	}
	
	public static void config(String msg){
		getLogger(Level.CONFIG).config(msg);
		message(msg);
	}
	
	public static void fine(String msg){
		getLogger(Level.FINE).fine(msg);
		message(msg);
	}
	
	public static void finer(String msg){
		getLogger(Level.FINER).finer(msg);
		message(msg);
	}
	
	public static void finest(String msg){
		getLogger(Level.FINEST).finest(msg);
		message(msg);
	}
	
	public static void severe(String msg){
		getLogger(Level.SEVERE).severe(msg);
		message(msg);
	}
	
	public static void warning(String msg){
		getLogger(Level.WARNING).warning(msg);
		message(msg);
	}
	
	public static void message(String msg){
		getLogger(Level.ALL).log(Level.ALL,msg);
	}
}
