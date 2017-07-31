package com.abelski.finalproject;

import java.io.IOException;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * This class is a Singleton class holding a reference to a Log4J Logger object,
 * that generates log strings to the logs.txt file.
 * In order to use this class the getInstance() method should be invoked. 
 * @author amit
 */
public class MyLogger {

	/**
	 * The singleton instance.
	 */
	private static MyLogger loggerInstance = null;
	
	/**
	 * The log4j Logger object.
	 */
	public final Logger logger = Logger.getLogger("appLogger");
	
	
	/**
	 * Private constructor. 
	 * Invoked when an object of this class is instantiated for the first time.
	 * Adding an appender with the appropriate file and layout.
	 */
	private MyLogger() {
		
		try {
			logger.addAppender(new FileAppender(new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} [%-10t]- %-5p - %m%n"), "logs.txt"));
		} catch (IOException e) {e.printStackTrace();}
		logger.setLevel(Level.ALL);
	}
	
	/**
	 * Retrieving the singleton instance
	 * (lazy initialization)
	 */
	public static MyLogger getInstance() {
		
		if(loggerInstance == null)	
		{
			loggerInstance = new MyLogger();
		}
		return loggerInstance;
	}
}
