package it.cryptochat.ui;

import it.cryptochat.utils.CryptoChatUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class CryptoChatClientMainUI {

	private Logger logger = Logger.getLogger(this.getClass()); 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Logger UI
		CryptoChatClientLoggerUI loggerUI = new CryptoChatClientLoggerUI();

		// Load properties
		CryptoChatClientMainUI main = new CryptoChatClientMainUI();
		main.initProperties();
		
		// Client UI
		CryptoChatClientUI client = new CryptoChatClientUI();
		loggerUI.setTitle(client.getClientName() + " Logger");
		
		// Dipslay (or not) logger
		Boolean loggerUIFlag = Boolean.valueOf(System.getProperty("LoggerUI"));
		loggerUI.setVisible(loggerUIFlag);
		
		client.setVisible(true);
	}

	public void initProperties() {

			InputStream is = this.getClass().getClassLoader().getResourceAsStream("cryptochat.properties");
			if(is == null) {
				logger.debug("Properties file not found. Creating default properties.");
				System.setProperty("CryptoMode", "NO_CRYPTO");
				System.setProperty("LoggerUI", "false");
			}
			else {
				CryptoChatUtils.loadPropertiesFromStream(is);
			}

	}
}
