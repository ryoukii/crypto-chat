package it.cryptochat.module;
import it.cryptochat.common.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;


public class SSLCryptoModule extends CryptoModule {

	private static SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
	private static SSLServerSocketFactory serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
	
//	@Override
//	public void send(DataOutputStream output, String message) {
//		try {
//			output.writeUTF(message);
//		} catch (IOException e) {
//			logger.error("Problems during message sending: " + e);
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public String read(DataInputStream input) {
//		try {
//			return input.readUTF();
//		} catch (IOException e) {
//			logger.error("Problems during message receiving: " + e);
//			e.printStackTrace();
//		}
//		
//		return null;
//	}
	
	@Override
	public void send(ObjectOutputStream output, Message message) {
		try {
			output.writeObject(message);
		} catch (IOException e) {
			logger.error("Problems during message sending: " + e);
			e.printStackTrace();
		}
	}

	@Override
	public Message read(ObjectInputStream input) throws IOException {
		try {
			Message msg = (Message) input.readObject();
			return msg;
		} catch (ClassNotFoundException e) {
			logger.error("Problems during message receiving: " + e);
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Socket createSocket(String serverAddress, int serverPort) throws UnknownHostException, IOException {
//		System.setProperty("javax.net.ssl.trustStore","mySrvKeystore");
//		System.setProperty("javax.net.ssl.trustStorePassword","123456");
		
		SSLSocket socket = (SSLSocket) socketFactory.createSocket(serverAddress, serverPort);
//		socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());

		return socket;
	}

	@Override
	public ServerSocket createServerSocket(int serverPort) throws IOException {
//		System.setProperty("javax.net.ssl.keyStore","mySrvKeystore");
//		System.setProperty("javax.net.ssl.keyStorePassword","123456");
		
		SSLServerSocket socket = (SSLServerSocket) serverSocketFactory.createServerSocket(serverPort);
//		socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());

		return socket; 
	}

}
