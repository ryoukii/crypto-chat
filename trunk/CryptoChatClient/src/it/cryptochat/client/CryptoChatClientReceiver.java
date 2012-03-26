package it.cryptochat.client;

import it.cryptochat.common.IMessage;
import it.cryptochat.module.CryptoModule;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import org.apache.log4j.Logger;


public class CryptoChatClientReceiver extends Thread {
	
	private Logger logger = Logger.getLogger(this.getClass());

	private CryptoModule cryptoModule;
	private ObjectInputStream input;
	private PrintStream ps;
	
	public CryptoChatClientReceiver(ObjectInputStream input, CryptoModule cryptoModule) {
		this(input, cryptoModule, System.out);
	}
	
	public CryptoChatClientReceiver(ObjectInputStream input, CryptoModule cryptoModule, PrintStream ps) {
		if(input != null)
			this.input = input;
		 
		if(cryptoModule != null)
			this.cryptoModule = cryptoModule;
		
		if(ps != null)
			this.ps = ps;
		else
			ps = System.out;
		
		logger.debug("Client receiver created");
	}
	
	@Override
	public void run() {
		
		Boolean stop = false;
		IMessage message;
		
		try {
			message = read();
			while(message != null && !stop) {
				ps.println(message);
				message = read();
			}
		} catch (IOException e) {
			stop = true;
			logger.error("Problems during message reading: " + e);
			e.printStackTrace();
		}
		
		logger.debug("ClientReceiver terminated");
	}
	
//	public String read() {
//		return cryptoModule.read(input);
//	}
	
	public IMessage read() throws IOException {
		return cryptoModule.read(input);
	}
}
