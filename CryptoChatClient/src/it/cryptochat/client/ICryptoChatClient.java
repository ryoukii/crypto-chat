package it.cryptochat.client;

import it.cryptochat.common.Message;

public interface ICryptoChatClient {

	public void connect(int serverPort, String serverAddress);
	public void send(Message message);
	public void close();
	public boolean isConnected();
}
