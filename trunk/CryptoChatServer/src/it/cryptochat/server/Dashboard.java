package it.cryptochat.server;
import it.cryptochat.common.Message;

import java.util.Observable;

import org.apache.log4j.Logger;


public class Dashboard extends Observable {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private static Dashboard instance = null;
	private Message message;
	
	public Dashboard() {
		message = new Message();
	}
	
	public String getMessage() {
		return message.getMessage();
	}
	
	synchronized public void append(String message) {
		this.message.setMessage(message);
		setChanged();
		notifyObservers();
		logger.debug("Message appended to dashboard: " + message);
	}
	
	public static Dashboard getInstance() {
		if(instance == null) {
			instance = new Dashboard();
		}
		
		return instance;
	}
}
