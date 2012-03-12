import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;


public class CryptoChatClient {

	private static final int SOCKET_TIMEOUT = 30000;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private int serverPort = -1;
	private InetAddress serverAddress = null;
	private Socket socket = null;
	private DataInputStream input = null;
	private DataOutputStream output = null;
	private String clientName;
	private CryptoModule criptoModule;
	
	public CryptoChatClient(int serverPort, String serverAddress, CryptoModuleFactory.ModuleType criptoMode) {
		this("Client", serverPort, serverAddress, criptoMode);
	}
	
	public CryptoChatClient(String clientName, int serverPort, String serverAddress, CryptoModuleFactory.ModuleType criptoMode) {
		
		setClientName(clientName);
		logger.debug("Client name: " + clientName);
		
		criptoModule = CryptoModuleFactory.getCriptoModule(criptoMode);
		
		if(serverPort >= 1024 && serverPort <= 65535) {
			this.serverPort = serverPort;
			
			try {
				this.serverAddress = InetAddress.getByName(serverAddress);
			} catch (UnknownHostException e) {
				logger.debug("Error in server address: " + e);
//				e.printStackTrace();
			}
		}
		else {
			logger.error("Port error: out of range 1024 - 65535 (" + serverPort + ")");
		}
		
		try {
			
//			socket = new Socket(serverAddress, serverPort);
			socket = criptoModule.createSocket(serverAddress, serverPort);
			socket.setSoTimeout(SOCKET_TIMEOUT);
			
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			
			CryptoChatClientReceiver clientReceiver = new CryptoChatClientReceiver(input, criptoModule);
			clientReceiver.setName(clientName);
			clientReceiver.start();

			send(clientName);
			
		} catch (UnknownHostException e) {
			logger.debug("Problems during socket creation: " + e);
//			e.printStackTrace();
		} catch (IOException e) {
			logger.debug("Problems during socket creation: " + e);
//			e.printStackTrace();
		}
		catch (Exception e) {
			logger.debug("Problems during socket creation: " + e);
			//		e.printStackTrace();
		}
	}
	
	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public void send(String message) {
//		try {
//			output.writeUTF(message);
//		} catch (IOException e) {
//			logger.error("Problems during message sending: " + e);
////			e.printStackTrace();
//		}
		
		criptoModule.send(output, message);
	}
	
	public void close() {
		try {
			socket.close();
			logger.debug("Socket closed");
		} catch (IOException e) {
			logger.error("Problems during socket closing: " + e);
//			e.printStackTrace();
		}
	}
}
