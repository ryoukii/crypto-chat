package it.cryptochat.server;
import it.cryptochat.module.CryptoModule;
import it.cryptochat.module.CryptoModuleFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.net.ssl.SSLSocket;

import org.apache.log4j.Logger;


public class CryptoChatMainServer extends Thread {
	
	private static final int STATIC_PORT = 54321;
	private static final int CLIENTSOCKET_TIMEOUT = 60000;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private int port;
	private ServerSocket serverSocket = null;
	private Dashboard dashboard;
	private CryptoModule cryptoModule;
	
	
	public CryptoChatMainServer(CryptoModuleFactory.ModuleType criptoMode) {
		this(STATIC_PORT, criptoMode);
	}

	public CryptoChatMainServer(int port, CryptoModuleFactory.ModuleType criptoMode) {
		
		setName("CrittoChatMainServer");

		dashboard = Dashboard.getInstance(); 
		
		cryptoModule = CryptoModuleFactory.getCriptoModule(criptoMode);
		
		if(port >= 1024 && port <= 65535)
			this.port = port;
		else {
			this.port = STATIC_PORT;
			logger.error("Port error: out of range 1024 - 65535 (" + port + ")");
		}
		
		logger.debug("Use port " + this.port);
		
		try {
//			serverSocket = new ServerSocket(this.port);
			serverSocket = cryptoModule.createServerSocket(this.port);
			serverSocket.setReuseAddress(true);
		} catch (IOException e) {
			logger.error("Prolems during server socket creation: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Prolems during server socket creation: " + e);
			e.printStackTrace();
		}
		
		logger.debug("Server socket created");
	}
	
	
	@Override
	public void run() {
		Socket clientSocket = null;
		
		while(true) {
			try {
				clientSocket = serverSocket.accept();
				clientSocket.setSoTimeout(CLIENTSOCKET_TIMEOUT);
				
				logger.debug("Connection accepted");
				
				new CryptoChatServer(clientSocket, dashboard, cryptoModule).start();
				
			} catch (IOException e) {
				logger.error("Problems in server thread: " + e);
				e.printStackTrace();
			}
		}
	}
}
