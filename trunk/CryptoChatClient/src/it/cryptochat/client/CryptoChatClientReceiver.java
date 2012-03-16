package it.cryptochat.client;

import it.cryptochat.common.Message;
import it.cryptochat.module.CryptoModule;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.apache.log4j.Logger;


public class CryptoChatClientReceiver extends Thread {
	
	private Logger logger = Logger.getLogger(this.getClass());

	private CryptoModule cryptoModule;
	private ObjectInputStream input;
	
	public CryptoChatClientReceiver(ObjectInputStream input, CryptoModule cryptoModule) {
		if(input != null)
			this.input = input;
		 
		if(cryptoModule != null)
			this.cryptoModule = cryptoModule;
		
		logger.debug("Client receiver created");
	}
	
	@Override
	public void run() {
		
		Message message;
		
		while((message = read()) != null) {
			System.out.println(message);
		}
	}
	
//	public String read() {
//		return cryptoModule.read(input);
//	}
	
	public Message read() {
		return cryptoModule.read(input);
	}
}
