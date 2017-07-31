package com.abelski.finalproject;

import java.awt.EventQueue;
/**
 * This application allows its users to get the up-to-date currency exchange rates 
 * for those currencies Israeli Bank provides.
 * it is capable of converting any sum in any currency into any other currency based on those rates.
 * @author amit
 */

public class Application {
	
	public static void main(String[] args) {

		MyLogger.getInstance().logger.info("Starting application..");
		// Initialize the controller and refreshing the data
		Refresher refresher = new Refresher();
		refresher.refresh();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Thread.currentThread().setName("WindowGUI");
					WindowGUI window = new WindowGUI(refresher); //WindowGUI gets the controller
					refresher.setView(window); //controller gets the view
				} catch (Exception e) {
					e.printStackTrace();
					MyLogger.getInstance().logger.fatal("There was a problem initializing the GUI");
				}
			}
		});
		// Infinite loop refreshing the application every 30 seconds or by user demand.
		refresher.cyclicRefresh();
	}
}