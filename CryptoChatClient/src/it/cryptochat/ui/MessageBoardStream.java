package it.cryptochat.ui;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 
 * @author Stefano
 *
 * Questa classe modella un PrintStream che consente di stampare
 * i messaggi su una MessageBoard
 */
public class MessageBoardStream extends PrintStream {

	private IMessageBoard messageBoard;
	
	public MessageBoardStream() {
		this(System.out);
	}
	
	public MessageBoardStream(OutputStream out) {
		super(out);
	}
	
	public IMessageBoard getMessageBoard() {
		return messageBoard;
	}

	public void setMessageBoard(IMessageBoard messageBoard) {
		this.messageBoard = messageBoard;
	}

	@Override
	public void print(String s) {
		messageBoard.appendToBoard(s);
	}
	
	@Override
	public void println() {
		print("\n");
	}
	
	@Override
	public void println(String x) {
		print(x + "\n");
	}
	
	@Override
	public void println(Object x) {
		String s = String.valueOf(x);
		print(s + "\n");
	}
	
}
