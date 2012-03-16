package it.cryptochat.module.test;

import it.cryptochat.module.CryptoModule;
import it.cryptochat.module.CryptoModuleFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class CryptoChatModuleTestUtils {

	// Commons
	protected static Logger logger = Logger.getLogger(CryptoChatModuleTestUtils.class);
	protected static int serverPort = 54321;
	protected static String serverAddress = "127.0.0.1";
	protected static final int SOCKET_TIMEOUT = 30000;
	
	// Client
	protected static Socket clientSocket = null;
	protected static ObjectInputStream clientInput = null;
	protected static ObjectOutputStream clientOutput = null;
	protected static String clientName = "Client1";
	protected static CryptoModule clientCryptoModule = CryptoModuleFactory.getCryptoModule(CryptoModuleFactory.ModuleType.NO_CRYPTO);
	protected static Client client;

	// Server
	protected static ServerSocket serverSocket = null;
	protected static Socket serverClientSocket = null;
	protected static ObjectInputStream serverInput = null;
	protected static ObjectOutputStream serverOutput = null;
	protected static CryptoModule serverCryptoModule = CryptoModuleFactory.getCryptoModule(CryptoModuleFactory.ModuleType.NO_CRYPTO);
	protected static Server server;
	
	private static Boolean clientReady = false;
	private static Boolean serverReady = false;
	
	
	public static void init() throws Exception {
		
//		setCryptoModule(CryptoModuleFactory.ModuleType.DH_DES);

		server = new Server();
		server.start();
		
		client = new Client();
		client.start();

		while(!clientReady || !serverReady);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		clientSocket.close();
		serverClientSocket.close();
		serverSocket.close();
		
		clientReady = false;
		serverReady = false;
	}
	
	protected static void setCryptoModule(CryptoModuleFactory.ModuleType cryptoMode) {
		clientCryptoModule = CryptoModuleFactory.getCryptoModule(cryptoMode);
		serverCryptoModule = CryptoModuleFactory.getCryptoModule(cryptoMode);
	}
	
	
	public static class Client extends Thread {
				
		public Client() {
			 this.setName(clientName);
		}
		
		@Override
		public void run() {
			try {

				clientSocket = clientCryptoModule.createSocket(serverAddress, serverPort);
				clientSocket.setSoTimeout(SOCKET_TIMEOUT);
				
				clientCryptoModule.init(clientSocket);
				
				clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
				clientInput = new ObjectInputStream(clientSocket.getInputStream());
				
				logger.debug("Client started");
				clientReady = true;
				
			} catch (UnknownHostException e) {
				logger.debug("Problems during socket creation: " + e);
				e.printStackTrace();
			} catch (IOException e) {
				logger.debug("Problems during socket creation: " + e);
				e.printStackTrace();
			}
			catch (Exception e) {
				logger.debug("Problems during socket creation: " + e);
				e.printStackTrace();
			}
		}
	}
	
	
	public static class Server extends Thread {
		
		public Server() {
			 this.setName("Server");
		}
		
		@Override
		public void run() {
			try {
				serverSocket = serverCryptoModule.createServerSocket(serverPort);
				serverSocket.setReuseAddress(true);
				
				serverClientSocket = serverSocket.accept();
				serverClientSocket.setSoTimeout(SOCKET_TIMEOUT);
				
				serverCryptoModule.init(serverClientSocket);
				
				serverOutput = new ObjectOutputStream(serverClientSocket.getOutputStream());
				serverInput = new ObjectInputStream(serverClientSocket.getInputStream());
				
				logger.debug("Server started");
				serverReady = true;
				
			} catch (IOException e) {
				logger.error("Prolems during server socket creation: " + e);
				e.printStackTrace();
			} catch (Exception e) {
				logger.error("Prolems during server socket creation: " + e);
				e.printStackTrace();
			}
		}
		
	}

}
