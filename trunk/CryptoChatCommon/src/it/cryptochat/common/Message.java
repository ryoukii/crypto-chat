package it.cryptochat.common;

import java.io.Serializable;

public class Message implements Serializable {
	
	private String sender = null;
	private String message = null;
	
	public Message() {
		this(null, null);
	}

	public Message(String sender, String message) {
		if(sender != null)
			this.sender = sender;
		
		if(message != null)
			this.message = message;
	}
	
	public String getSender() {
		return sender;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return new String(sender + ": " + message);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    
	    final Message other = (Message) obj;
	    
	    if (!this.getMessage().equals(other.getMessage()) || !this.getSender().equals(other.getSender()))
	        return false;
	    
	    return true;
	}
}
