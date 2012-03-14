package it.cryptochat.module;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Logger;


public abstract class CryptoModule {
	
	protected Logger logger;
	
	public CryptoModule() {
		this(null);
	}
	
	public CryptoModule(Logger logger) {
		if(logger != null)
			this.logger = logger;
		else
			this.logger = Logger.getLogger(this.getClass());
	}
	
	public Socket createSocket(String serverAddress, int serverPort) throws Exception {
		return new Socket(serverAddress, serverPort);
	}
	
	public ServerSocket createServerSocket(int serverPort) throws Exception {
		return new ServerSocket(serverPort);
	}
	
	public void init(Socket socket) {};
	
	public abstract void send(DataOutputStream output, String message);
	public abstract String read(DataInputStream input);
}
