import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


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
		
		CryptoChatMainServer mainServer = new CryptoChatMainServer(serverPort, CryptoModuleFactory.ModuleType.RSA);
		mainServer.start();
		
		
		
		// Client
		
//		CrittoChatClient client = new CrittoChatClient(serverPort, serverAddress);
//		
//		try {
//			System.out.println("Messaggio: ");
//			while((inputData = stdIn.readLine()) != null) {
//				client.send(inputData);
//				if(inputData.equals(END_CMD))
//					break;
//				System.out.println("Ricevuto: " + client.read());
//			}
//			client.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}

}
