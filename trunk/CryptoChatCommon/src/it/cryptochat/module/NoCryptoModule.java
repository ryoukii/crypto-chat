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


public class NoCryptoModule extends CryptoModule {

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
			output.flush();
		} catch (IOException e) {
			logger.error("Problems during message sending: " + e);
			e.printStackTrace();
		}
	}
	
	@Override
	public Message read(ObjectInputStream input) {
		
		try {
			Message msg = (Message) input.readObject();
			return msg;
		} catch (IOException e) {
			logger.error("Problems during message receiving: " + e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			logger.error("Problems during message receiving: " + e);
			e.printStackTrace();
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
