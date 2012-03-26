package it.cryptochat.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class CryproChatConnectUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tfServerAddress;
	private JTextField tfServerPort;
	private JLabel lbServerAddress;
	private JLabel lbServerPort;
	private JButton btnConnect;
	private JButton btnCancel;
	private boolean connectPressed;

	public CryproChatConnectUI(Frame parent) {
		super(parent, "Login", true);
		//
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.HORIZONTAL;

		lbServerAddress = new JLabel("Server address: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(lbServerAddress, cs);

		tfServerAddress = new JTextField(20);
		tfServerAddress.setText("127.0.0.1");
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		panel.add(tfServerAddress, cs);

		lbServerPort = new JLabel("Server port: ");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(lbServerPort, cs);

		tfServerPort = new JTextField(20);
		tfServerPort.setText("54321");
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		panel.add(tfServerPort, cs);
		panel.setBorder(new LineBorder(Color.GRAY));


		btnConnect = new JButton("Connect");

		btnConnect.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				connectPressed = true;
				dispose();
			}
		});
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectPressed = false;
				dispose();
			}
		});
		JPanel bp = new JPanel();
		bp.add(btnConnect);
		bp.add(btnCancel);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(bp, BorderLayout.PAGE_END);

		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}

	public String getServerAddress() {
		return tfServerAddress.getText().trim();
	}

	public String getServerPort() {
		return new String(tfServerPort.getText().trim());
	}

	public boolean isConnectPressed() {
		return connectPressed;
	}

}
