package utils;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class Logger {
	
	private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("J4Life Log");
	private static FileHandler fh;
	
	private static void setLevel(Level level) {
		
		
		try {
			if(fh==null){
				fh = new FileHandler("J4Life.log", true);
				logger.addHandler(fh);
				SimpleFormatter formatter = new SimpleFormatter();
				fh.setFormatter(formatter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.setLevel(level);
	}
	
	public static void info(String msg){
		setLevel(Level.INFO);
		logger.info(msg);
	}
	
	public static void config(String msg){
		setLevel(Level.CONFIG);
		logger.config(msg);
	}
	
	public static void fine(String msg){
		setLevel(Level.FINE);
		logger.fine(msg);
	}
	
	public static void finer(String msg){
		setLevel(Level.FINER);
		logger.finer(msg);
	}
	
	public static void finest(String msg){
		setLevel(Level.FINEST);
		logger.finest(msg);
	}
	
	public static void severe(String msg){
		setLevel(Level.SEVERE);
		logger.severe(msg);
	}
	
	public static void warning(String msg){
		setLevel(Level.WARNING);
		logger.warning(msg);
	}
	
	public static void message(String msg){
		setLevel(Level.ALL);
		logger.log(Level.ALL,msg);
	}
}
