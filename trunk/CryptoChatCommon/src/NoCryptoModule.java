import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class NoCryptoModule extends CryptoModule {

	@Override
	public void send(DataOutputStream output, String message) {
		try {
			output.writeUTF(message);
		} catch (IOException e) {
			logger.error("Problems during message sending: " + e);
//			e.printStackTrace();
		}
	}

	@Override
	public String read(DataInputStream input) {
		try {
			return input.readUTF();
		} catch (IOException e) {
			logger.error("Problems during message receiving: " + e);
//			e.printStackTrace();
		}
		
		return null;
	}

//	@Override
//	public Socket createSocket(String serverAddress, int serverPort) throws Exception {
//		return new Socket(serverAddress, serverPort);
//	}
//
//	@Override
//	public ServerSocket createServerSocket(int serverPort) throws Exception {
//		return new ServerSocket(serverPort);
//	}

}
