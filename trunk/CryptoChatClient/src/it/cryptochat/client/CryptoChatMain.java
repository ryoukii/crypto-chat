package it.cryptochat.client;

import it.cryptochat.common.Message;
import it.cryptochat.module.CryptoModuleFactory;
import it.cryptochat.module.CryptoModuleFactory.ModuleType;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;



public class CryptoChatMain {

	private Logger logger = Logger.getLogger("CryptoChatClient");
	private static final String END_CMD = "end;";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int serverPort = 54321;
		String serverAddress = "127.0.0.1";
		String inputData;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		CryptoChatMain cryptoChatClient = new CryptoChatMain();
		
		cryptoChatClient.initProperties();
		
		// Client
		
		CryptoChatClient client;
//		CryptoModuleFactory.ModuleType moduleType = CryptoModuleFactory.ModuleType.NO_CRYPTO;
		CryptoModuleFactory.ModuleType moduleType = CryptoModuleFactory.ModuleType.valueOf(System.getProperty("CryptoMode"));
		
		if(args.length == 1) {
			client = new CryptoChatClient(args[0], serverPort, serverAddress, moduleType);
		}
		else {
			client = new CryptoChatClient(serverPort, serverAddress, moduleType);
		}
		
		try {
			System.out.print("Messaggio: ");
			while((inputData = stdIn.readLine()) != null) {
				client.send(new Message(client.getClientName(), inputData));
				if(inputData.equals(END_CMD))
					break;
//				System.out.println("Ricevuto: " + client.read());
				System.out.print("Messaggio: ");
			}
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void initProperties() {

		Properties props = new Properties();

		try {

//			FileInputStream fis = new FileInputStream("./cryptochat.properties");
//			InputStream is = this.getClass().getResourceAsStream("/cryptochat.properties");
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("cryptochat.properties");
			if(is == null) {
				logger.debug("Properties file not found. Creating default properties.");
				System.setProperty("CryptoMode", "DH_DES");
			}
			else {
				logger.debug("Properties file found. Loading properties.");
				props.load(is);
				String propString = props.getProperty("CryptoMode", "NO_CRYPTO");
				System.setProperty("CryptoMode", propString);
			}

		} catch (IOException e) {
			logger.error("Problems loading properties");
			e.printStackTrace();
		}
		
		logger.debug("Properties used:");
		logger.debug("CryptoMode = " + System.getProperty("CryptoMode"));

	}

}
