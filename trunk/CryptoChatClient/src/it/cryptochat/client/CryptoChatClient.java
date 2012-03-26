package it.cryptochat.client;

import it.cryptochat.common.*;
import it.cryptochat.module.CryptoModule;
import it.cryptochat.module.CryptoModuleFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;


public class CryptoChatClient implements ICryptoChatClient {

//	private static final int SOCKET_TIMEOUT = 30000;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private Socket socket = null;
	private ObjectInputStream input = null;
	private ObjectOutputStream output = null;
	private String clientName;
	private CryptoModule cryptoModule;
	private PrintStream ps;
	
//	public CryptoChatClient(int serverPort, String serverAddress, CryptoModuleFactory.ModuleType criptoMode) {
//		this("Client", serverPort, serverAddress, criptoMode, System.out);
//	}
//	
//	public CryptoChatClient(String clientName, int serverPort, String serverAddress, CryptoModuleFactory.ModuleType criptoMode) {
//		this(clientName, serverPort, serverAddress, criptoMode, System.out);
//	}
//	
//	public CryptoChatClient(String clientName, int serverPort, String serverAddress, CryptoModuleFactory.ModuleType cryptoMode, PrintStream ps) {
//		
//		setClientName(clientName);
//		logger.debug("Client name: " + clientName);
//		
//		cryptoModule = CryptoModuleFactory.getCryptoModule(cryptoMode);
//		
//		if(serverPort >= 1024 && serverPort <= 65535) {
//			this.serverPort = serverPort;
//			
//			try {
//				this.serverAddress = InetAddress.getByName(serverAddress);
//			} catch (UnknownHostException e) {
//				logger.error("Error in server address: " + e);
//				e.printStackTrace();
//			}
//		}
//		else {
//			logger.error("Port error: out of range 1024 - 65535 (your choice is " + serverPort + ")");
//		}
//		
//		try {
//			
//			socket = cryptoModule.createSocket(serverAddress, serverPort);
////			socket.setSoTimeout(SOCKET_TIMEOUT);
//			
//			cryptoModule.init(socket);
//			
//			output = new ObjectOutputStream(socket.getOutputStream());
//			input = new ObjectInputStream(socket.getInputStream());
//			
//			CryptoChatClientReceiver clientReceiver = new CryptoChatClientReceiver(input, cryptoModule, ps);
//			clientReceiver.setName(clientName);
//			clientReceiver.start();
//
//			send(new Message(clientName, "Hello"));
//			
//		} catch (UnknownHostException e) {
//			logger.error("Problems during socket creation: " + e);
//			e.printStackTrace();
//		} catch (IOException e) {
//			logger.error("Problems during socket creation: " + e);
//			e.printStackTrace();
//		} catch (Exception e) {
//			logger.error("Problems during socket creation: " + e);
//			e.printStackTrace();
//		}
//	}
	
	public CryptoChatClient(CryptoModuleFactory.ModuleType criptoMode) {
		this("Client", criptoMode, System.out);
	}
	
	public CryptoChatClient(String clientName, CryptoModuleFactory.ModuleType criptoMode) {
		this(clientName, criptoMode, System.out);
	}
	
	public CryptoChatClient(String clientName, CryptoModuleFactory.ModuleType cryptoMode, PrintStream ps) {
		
		setClientName(clientName);
		logger.debug("Client name: " + clientName);
		
		cryptoModule = CryptoModuleFactory.getCryptoModule(cryptoMode);
		
		if(ps != null)
			this.ps = ps;
	}
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

//	public void send(String message) {
//		cryptoModule.send(output, message);
//	}
	
	public void send(Message message) {
		cryptoModule.send(output, message);
	}
	
	public void connect(int serverPort, String serverAddress) {
		
		if(serverPort < 1024 || serverPort > 65535) {
			logger.error("Port error: out of range 1024 - 65535 (your choice is " + serverPort + ")");
			serverPort = 54321;
		}
		
		try {

			socket = cryptoModule.createSocket(serverAddress, serverPort);
//			socket.setSoTimeout(SOCKET_TIMEOUT);

			cryptoModule.init(socket);

			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());

			CryptoChatClientReceiver clientReceiver = new CryptoChatClientReceiver(input, cryptoModule, ps);
			clientReceiver.setName(clientName);
			clientReceiver.start();

			send(new Message(clientName, "Hello"));

		} catch (UnknownHostException e) {
			logger.error("Problems during socket creation: " + e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("Problems during socket creation: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("Problems during socket creation: " + e);
			e.printStackTrace();
		}
	}
	
	public void close() {
		if(socket != null && !socket.isClosed()) {
			try {
				socket.close();
				logger.debug("Socket closed");
			} catch (IOException e) {
				logger.error("Problems during socket closing: " + e);
				e.printStackTrace();
			}
		}
	}
	
	public boolean isConnected() {
		if(socket == null)
			return false;
		
		return socket.isConnected();
	}
}
