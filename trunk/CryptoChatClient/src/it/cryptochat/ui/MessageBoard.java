package it.cryptochat.ui;

import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class MessageBoard implements IMessageBoard {
	
	private JTextArea boardTA;

	public MessageBoard() {
		this(new JTextArea());
	}
	
	public MessageBoard(JTextArea board) {
		
		boardTA = board;
		boardTA.setEditable(false);
		boardTA.setLineWrap(true);
		boardTA.setWrapStyleWord(true);
		
		boardTA.setBorder(new TitledBorder("Messages"));
	}
	
	public JTextArea getBoardTA() {
		return boardTA;
	}
	
	synchronized public void appendToBoard(String message) {
		boardTA.append(message);
	}
}
