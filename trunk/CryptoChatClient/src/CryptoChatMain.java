import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;


public class CryptoChatMain {

	private static final String END_CMD = "end;";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int serverPort = 54321;
		String serverAddress = "127.0.0.1";
		String inputData;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		
		// Server 
		
//		CrittoChatMainServer mainServer = new CrittoChatMainServer(serverPort);
//		mainServer.start();
		
		
		
		// Client
		
		CryptoChatClient client;
		
		if(args.length == 1) {
			client = new CryptoChatClient(args[0], serverPort, serverAddress, CryptoModuleFactory.ModuleType.RSA);
		}
		else {
			client = new CryptoChatClient(serverPort, serverAddress, CryptoModuleFactory.ModuleType.RSA);
		}
		
		try {
			System.out.print("Messaggio: ");
			while((inputData = stdIn.readLine()) != null) {
				client.send(inputData);
				if(inputData.equals(END_CMD))
					break;
//				System.out.println("Ricevuto: " + client.read());
				System.out.print("Messaggio: ");
			}
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
