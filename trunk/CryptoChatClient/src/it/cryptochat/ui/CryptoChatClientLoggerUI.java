package it.cryptochat.ui;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

public class CryptoChatClientLoggerUI extends JFrame {

	private Logger logger = Logger.getLogger(this.getClass()); 
	private JTextArea textArea = new JTextArea();
	private JPanel panel = new JPanel();
	
	public CryptoChatClientLoggerUI() {
		
		setTitle("Logger");
		
		TextAreaAppender.setTextArea(textArea);
//		Logger.getRootLogger().addAppender(new TextAreaAppender());
		
		setSize(500, 300);
		
		GridBagLayout bagLayout = new GridBagLayout();
		GridBagConstraints limit = new GridBagConstraints();
		panel.setLayout(bagLayout);
		
		// Text Area
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		textArea.setBorder(new TitledBorder("Debug console"));
		
		JScrollPane scroller = new JScrollPane(textArea);
		scroller.setPreferredSize(new Dimension(300, 100));
		setLimit(limit, 0, 0, 1, 1, 1, 1);
		limit.fill = GridBagConstraints.BOTH;
		limit.anchor = GridBagConstraints.WEST;
		bagLayout.setConstraints(scroller, limit);
		
		panel.add(scroller);
		setContentPane(panel);
	}
	
	private void setLimit(GridBagConstraints gbc, int gx, int gy, int gw, int gh, double wx, double wy) {

		gbc.gridx = gx;
		gbc.gridy = gy;
		gbc.gridwidth = gw;
		gbc.gridheight = gh;
		gbc.weightx = wx;
		gbc.weighty = wy;

	}
}
