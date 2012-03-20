package it.cryptochat.ui;

import it.cryptochat.client.CryptoChatClient;
import it.cryptochat.common.Message;
import it.cryptochat.module.CryptoModuleFactory;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

public class CryptoChatClientUI extends JFrame {
	
	private Logger logger = Logger.getLogger(this.getClass()); 
	private JButton sendBt = new JButton("Send");
	private MessageBoard board = new MessageBoard();
	private JTextField messageTF = new JTextField();
	private JLabel boardLabel = new JLabel("Board", SwingConstants.CENTER);
	private JLabel messageLabel = new JLabel("Message", SwingConstants.CENTER);
	private JPanel panel = new JPanel();
	private JMenuBar menuBar = new JMenuBar();
	private CryptoChatClient client;
	private CryproChatLoginUI login;
	private CryptoChatClientUI thisInstance = null;
	
	public CryptoChatClientUI() {
		
		super("CryptoChat");
		
		login = new CryproChatLoginUI(this);
		login.setVisible(true);
		
		// Create Client
//		CryptoModuleFactory.ModuleType moduleType = CryptoModuleFactory.ModuleType.valueOf(System.getProperty("CryptoMode"));
		CryptoModuleFactory.ModuleType moduleType = CryptoModuleFactory.ModuleType.valueOf(login.getCryptoMode());
		logger.info("CryptoMode selected by user: " + moduleType);
		MessageBoardStream jtaps;
		jtaps = new MessageBoardStream();
		jtaps.setMessageBoard(board);
		client = new CryptoChatClient(login.getUsername(), moduleType, jtaps);
		
		setTitle("CryptoChat: " + client.getClientName());
		
		setSize(500, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		messageTF.setEditable(true);
		
		GridBagLayout bagLayout = new GridBagLayout();
		GridBagConstraints limit = new GridBagConstraints();
		panel.setLayout(bagLayout);
		
		// Menu bar
		JMenu connectionMenu = new JMenu("Connection");
		JMenuItem connectItem = new JMenuItem("Connect");
		JMenuItem disconnectItem = new JMenuItem("Disconnect");
		connectionMenu.add(connectItem);
		connectionMenu.add(disconnectItem);
		menuBar.add(connectionMenu);
		this.setJMenuBar(menuBar);
		connectItem.addActionListener(new ConnectListener());
		disconnectItem.addActionListener(new DisconnectListener());
		
		// Label board
		setLimit(limit, 0, 0, 1, 1, 1, 1);
		limit.fill = GridBagConstraints.NONE;
		limit.anchor = GridBagConstraints.WEST;
		bagLayout.setConstraints(boardLabel, limit);
		panel.add(boardLabel);

		// Text Area board
		JScrollPane scroller = new JScrollPane(board.getBoardTA());
		scroller.setPreferredSize(new Dimension(300, 100));
		setLimit(limit, 0, 1, 2, 1, 1, 1);
		limit.fill = GridBagConstraints.BOTH;
		limit.anchor = GridBagConstraints.WEST;
		bagLayout.setConstraints(scroller, limit);
		panel.add(scroller);
		
		// Lable message
		setLimit(limit, 0, 2, 1, 1, 1, 1);
		limit.fill = GridBagConstraints.NONE;
		limit.anchor = GridBagConstraints.WEST;
		bagLayout.setConstraints(messageLabel, limit);
		panel.add(messageLabel);
		
		// Text Field message
		setLimit(limit, 0, 3, 1, 1, 0.2, 0.2);
		limit.fill = GridBagConstraints.HORIZONTAL;
		limit.anchor = GridBagConstraints.NORTHWEST;
		bagLayout.setConstraints(messageTF, limit);
		panel.add(messageTF);
		
		// Button send
		setLimit(limit, 1, 3, 1, 1, 0, 0.5);
		limit.fill = GridBagConstraints.NONE;
		limit.anchor = GridBagConstraints.NORTHWEST;
		bagLayout.setConstraints(sendBt, limit);
		panel.add(sendBt);
		sendBt.addActionListener(new SendBtListener());
		
		setContentPane(panel);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
//				showInfoMessage("Closing");
				if(client != null && client.isConnectet())
					client.close();
			}
		});
		
		
		thisInstance = this;
	}


	private void setLimit(GridBagConstraints gbc, int gx, int gy, int gw, int gh, double wx, double wy) {

		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;

	}
	
	
	public void showInfoMessage(String message) {
		JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public String getClientName() {
		return client.getClientName();
	}
	
	
	
	
	
	// ActionListeners
	
	private class SendBtListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	String message = messageTF.getText();
			if(!message.equals("")) {
				if(client.isConnectet()) {
					client.send(new Message(client.getClientName(), message));
					board.appendToBoard("You: " + message + "\n");
				}
			}
        }
    }
	
	private class ConnectListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	CryproChatConnectUI connectDialog = new CryproChatConnectUI(thisInstance);
        	connectDialog.setVisible(true);
        	if(connectDialog.isConnectPressed())
        		client.connect(Integer.valueOf(connectDialog.getServerPort()), connectDialog.getServerAddress());
        }
    }
	
	private class DisconnectListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	client.close();
        }
    }
}
