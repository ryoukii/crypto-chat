package it.cryptochat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

public class CryptoChatUtils {
	
	private static Logger logger = Logger.getLogger("CryptoChatUtils");

	public static void loadPropertiesFromStream(InputStream is) {
		
		logger.info("----- Loading props -----");
		
		Properties props = new Properties();
		
		try {
			
			props.load(is);
			
			Enumeration keys = props.keys();
			while (keys.hasMoreElements()) {
			  String key = (String)keys.nextElement();
			  String value = (String)props.get(key);
			  System.setProperty(key, value);
			  logger.info(key + ": " + value);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("----- Loading props ended -----");

	}
}
