package it.cryptochat.server;
import it.cryptochat.module.CryptoModuleFactory;
import it.cryptochat.utils.CryptoChatUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;


public class CryptoChatMain {

	private Logger logger = Logger.getLogger("CryptoChatServer");
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int serverPort = 54321;
		CryptoChatMain cryptoCharServer = new CryptoChatMain();
		
		cryptoCharServer.initProperties();
		
		// Server 
		
		CryptoModuleFactory.ModuleType moduleType = CryptoModuleFactory.ModuleType.valueOf(System.getProperty("CryptoMode"));
		CryptoChatMainServer mainServer = new CryptoChatMainServer(serverPort, moduleType);
		mainServer.start();
		
	}


	public void initProperties() {

		InputStream is = this.getClass().getClassLoader().getResourceAsStream("cryptochat.properties");
		if(is == null) {
			logger.debug("Properties file not found. Creating default properties.");
			System.setProperty("CryptoMode", "NO_CRYPTO");
		}
		else {
			CryptoChatUtils.loadPropertiesFromStream(is);
		}

	}

}
