package com.abelski.finalproject;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import junit.framework.TestCase;

/**
 * This is a test class of the CurrencyDataParser class, tested with JUnit.
 * The tested methods are testParseXMLFile() and WriteXMLToFile() 
 * that throw Exceptions when an error occurs
 * if an exception is thrown, the test fails.
 * @author amit
 */
public class CurrencyDataParserTest extends TestCase {

	CurrencyDataParser parser = new CurrencyDataParser();

	public final void testWriteXMLToFile() {
		/*
		 * to test the method, we simply call the writeXMLToFile() method
		 * that should throw an IOException on error,
		 * if an exception in thrown, we simply catch it and call to fail() with failure message.
		 * else, we proceed to assertTrue() and true parameter so that the test will succeed.
		 */
		try {
			parser.writeXMLToFile();
			assertTrue("Method didnt throw exception, test succeeded..", true);
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception was thrown, test failed..");
		}
	}
	
	public final void testParseXMLFile() {
		/*
		 * to test the method, we simply call the parseXMLFile() method
		 * that should throw exceptions on error,
		 * if an exception in thrown, we simply catch it and call to fail() with failure message.
		 * else, we proceed to assertTrue() and true parameter so that the test will succeed.
		 */
		try {
			parser.parseXMLFile();
			assertTrue("Method didnt throw exception, test succeeded..", true);
		} catch (IOException e) {
			e.printStackTrace();
			fail("IOException was thrown, test failed..");
		} catch (SAXException e) {
			e.printStackTrace();
			fail("SAXException was thrown, test failed..");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			fail("ParserConfigurationException was thrown, test failed..");
		}
	}
}