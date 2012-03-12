import java.io.DataInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;


public class CryptoChatClientReceiver extends Thread {
	
	private Logger logger = Logger.getLogger(this.getClass());

	private CryptoModule criptoModule;
	private DataInputStream input;
	
	public CryptoChatClientReceiver(DataInputStream input, CryptoModule criptoModule) {
		if(input != null)
			this.input = input;
		 
		if(criptoModule != null)
			this.criptoModule = criptoModule;
	}
	
	@Override
	public void run() {
		
		String message;
		
		while((message = read()) != null) {
			System.out.println("Ricevuto: " + message);
		}
	}
	
	public String read() {
//		try {
//			return input.readUTF();
//		} catch (IOException e) {
//			logger.error("Problems during message receiving: " + e);
////			e.printStackTrace();
//		}
//		
//		return null;
		
		return criptoModule.read(input);
	}
}
