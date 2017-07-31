package com.abelski.finalproject;

import java.io.IOException;
import java.util.Vector;

/**
 * The controller part of the mvc pattern, 
 * Holds both windowGUI and CurrencyDataParser, responsible to connect between the two
 * and refresh the each 30 seconds or on user demand.
 * @author amit
 */
public class Refresher {

	private WindowGUI windowGui;
	private CurrencyDataParser parser;
	/**
	 * Contains the data from the XML file
	 */
	private Vector<Vector<String>> tableData;
	
	/**
	 * refresh on demand flag
	 */
	private boolean refreshFlag;
	
	/**
	 * Controller's constructor, setting CurrencyDataParser and myLogger references.
	 */
	public Refresher() {
		
		parser = new CurrencyDataParser();
		refreshFlag = false;
	}
	
	/**
	 * Refreshes the application by calling
	 * parser's writeXMLToFile() , parseXMLFile() and windowGui's update()
	 * synchronized disables the possibility of two threads writing to file at the same time
	 */
	public synchronized void refresh() {
		
		MyLogger.getInstance().logger.info("Refreshing data");
		
		try{
			MyLogger.getInstance().logger.info("Getting data from the net");
			parser.writeXMLToFile();
		}
		catch(IOException e){
			e.printStackTrace();
			MyLogger.getInstance().logger.error("There was a problem connecting to server");
		}
		try {
			tableData = parser.parseXMLFile();
		}
		catch(Exception e){e.printStackTrace();}
		
		if(windowGui != null)
			windowGui.update();
	}
	
	/**
	 * Infinite loop refreshing the application every 30 seconds or by user demand.
	 */
	public void cyclicRefresh() {
		
		long time = System.currentTimeMillis();
		while(true)
		{	
			if(System.currentTimeMillis() - time > 30000)
			{
				refresh();
				time = System.currentTimeMillis();
			}

			try { // without sleep, bugs occur, possibly something's being missed in the micro-commands.? 
				Thread.sleep(10);
			} catch (InterruptedException e) {e.printStackTrace();}

			if(getRefreshFlag())
			{
				refresh();
				setRefreshFlag(false);
			}
		}
	}
	
	//some getters and setters
	public void setView(WindowGUI v) {windowGui = v;}
	public void setParser(CurrencyDataParser m) {parser = m;}
	public void setRefreshFlag(boolean b) {refreshFlag = b;}
	public Vector<Vector<String>> getTableData() {return tableData;}
	public String getCurrencyDate() {return parser.getCurrencyDate();}
	public boolean getRefreshFlag() {return refreshFlag;}
}