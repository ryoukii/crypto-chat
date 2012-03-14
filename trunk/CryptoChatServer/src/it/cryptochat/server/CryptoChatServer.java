package it.cryptochat.server;
import it.cryptochat.module.CryptoModule;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import org.apache.log4j.Logger;


public class CryptoChatServer extends Thread implements Observer {

	private static final String STOP_STRING = "end;"; 
	private Logger logger = Logger.getLogger(this.getClass());
	
	private boolean stop = false;
	
	private Socket clientSocket = null;
	private DataInputStream input = null;
	private DataOutputStream output = null;
	private Dashboard dashboard;
	private CryptoModule cryptoModule;
	
	public CryptoChatServer(Socket clientSocket, Dashboard dashboard, CryptoModule cryptoModule) {
		
		this.cryptoModule = cryptoModule;
		
		if(clientSocket != null) {
			this.clientSocket = clientSocket;
			
			try {
				
				input = new DataInputStream(clientSocket.getInputStream());
				output = new DataOutputStream(clientSocket.getOutputStream());
				
//				this.setName(input.readUTF());
				
			} catch (IOException e) {
				logger.error("Problems during streams opening: " + e);
				e.printStackTrace();
			}
			
		}
		else {
			logger.error("Client socket can't be null");
		}
		
		logger.debug("Streams created");
		
		if(dashboard != null) {
			this.dashboard = dashboard;
			dashboard.addObserver(this);
		}
		
	}
	
	@Override
	public void run() {
		
		String buffer;
		
		cryptoModule.init(clientSocket);
		
		while(!stop) {
			buffer = cryptoModule.read(input);
			dashboard.append(buffer);
			
			if(buffer.equals(STOP_STRING)) {
				logger.debug("Stop command received");
				stop = true;
				break;
			}
		}
		
		try {
			clientSocket.close();
			logger.debug("Client socket closed");
		} catch (IOException e) {
			logger.error("Problems dunring socket closing " + e);
			e.printStackTrace();
		}
	}
	
	private void send(String message) throws IOException {
//		output.writeUTF(message);
		cryptoModule.send(output, message);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		try {
			String message = dashboard.getMessage();
			send(message);
			logger.debug("Message sent to " + this.getName() + ": " + message);
		} catch (IOException e) {
			logger.error("Problems during message sending: " + e);
			e.printStackTrace();
		}
	}
	
}
