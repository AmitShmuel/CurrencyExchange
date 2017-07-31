package com.abelski.finalproject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A class that represents the Model of the mvc pattern,
 * this class is responsible for getting the data from the XML page provided
 * by the Bank of Israel website, and store it in "data.xml" file.
 * this class also responsible for parsing the XML file and creating a Vector of Strings' vectors
 * each Strings' vector represents a currency.
 * 
 * @author amit
 */
public class CurrencyDataParser {

	/**
	 * Holds the last date the data was updated
	 */
	private String currencyDate;
	
	public String getCurrencyDate() {return currencyDate;}
	
	public void writeXMLToFile() throws IOException {

		InputStream is = null;
		FileOutputStream fos = null;
		HttpURLConnection con = null;
		URL url = null;
			
		try { // connecting
			url = new URL("http://www.boi.org.il/currency.xml");
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			is = con.getInputStream();
			fos = new FileOutputStream("data.xml");
			
			streamCopy(is, fos); // copying XML DOM to the file
		} 
		catch (IOException e) {e.printStackTrace(); throw e ;} 
		finally
		{
			if(is!=null)
			{
				try	{is.close();}
				catch(IOException e){e.printStackTrace();}
			}
			if(fos!=null)
			{
				try	{fos.close();}
				catch(IOException e){e.printStackTrace();}
			}
			if(con!=null)
				con.disconnect();
		}
	}
	
	/**
	 * Parsing the XML file and creating a Vector of Strings' Vector
	 * each String vector represents a currency.
	 * this vector returns to the controller.
	 * @return 
	 */
	public Vector<Vector<String>> parseXMLFile() throws IOException, 
														SAXException,
														ParserConfigurationException {
		
		MyLogger.getInstance().logger.info("Parsing XML file");
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		@SuppressWarnings("unchecked")
		Vector<String>[] vecArr = new Vector[100];
		
		try {
			
			File file = new File("data.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			
			currencyDate = doc.getElementsByTagName("LAST_UPDATE").item(0).getTextContent();
			NodeList nList = doc.getElementsByTagName("CURRENCY");
			int len = nList.getLength();
			for (int temp = 0; temp < len; temp++) 
			{
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{
					Element e = (Element) nNode;
								
					vecArr[temp] = new Vector<>();
					vecArr[temp].add(e.getElementsByTagName("NAME").item(0).getTextContent());
					vecArr[temp].add(e.getElementsByTagName("UNIT").item(0).getTextContent());
					vecArr[temp].add(e.getElementsByTagName("CURRENCYCODE").item(0).getTextContent());
					vecArr[temp].add(e.getElementsByTagName("COUNTRY").item(0).getTextContent());
					vecArr[temp].add(e.getElementsByTagName("RATE").item(0).getTextContent());
					vecArr[temp].add(e.getElementsByTagName("CHANGE").item(0).getTextContent());
					
					data.add(vecArr[temp]);
				}
			}
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace(); 
			MyLogger.getInstance().logger.fatal("There was a problem building document");
		} 
		catch (SAXException e) {
			e.printStackTrace(); 
			MyLogger.getInstance().logger.fatal("There was a problem parsing the file");
		} 
		catch (IOException e) {
			e.printStackTrace(); 
			MyLogger.getInstance().logger.fatal("There was a problem parsing the file");
		}
		
		return data;
	}
	
	public void streamCopy(InputStream in, OutputStream out) throws IOException    	
	{
		synchronized(out)
		{
			synchronized(in)
			{
				byte vec[] = new byte[256];
				int numOfBytes = in.read(vec);
				while(numOfBytes != -1)
				{
					out.write(vec,0,numOfBytes);
					numOfBytes = in.read(vec);
				}
			}
		}
	}
}