package it.cryptochat.ui;

import it.cryptochat.module.CryptoModuleFactory;
import it.cryptochat.module.CryptoModuleFactory.ModuleType;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class CryproChatLoginUI extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tfUsername;
//	private JPasswordField pfPassword;
	private JComboBox<String> cbCyptoMode;
	private JLabel lbUsername;
//	private JLabel lbPassword;
	private JLabel lbCryptoMode;
	private JButton btnLogin;
	private JButton btnCancel;
	private boolean succeeded;

	public CryproChatLoginUI(Frame parent) {
		super(parent, "Login", true);
		//
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.HORIZONTAL;

		lbUsername = new JLabel("Username: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(lbUsername, cs);

		tfUsername = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		panel.add(tfUsername, cs);

//		lbPassword = new JLabel("Password: ");
//		cs.gridx = 0;
//		cs.gridy = 1;
//		cs.gridwidth = 1;
//		panel.add(lbPassword, cs);
//
//		pfPassword = new JPasswordField(20);
//		cs.gridx = 1;
//		cs.gridy = 1;
//		cs.gridwidth = 2;
//		panel.add(pfPassword, cs);
//		panel.setBorder(new LineBorder(Color.GRAY));
		
		lbCryptoMode = new JLabel("Cryptography: ");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(lbCryptoMode, cs);

		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		for(ModuleType moduleType : CryptoModuleFactory.ModuleType.values())
			model.addElement(moduleType.name());
//		model.addElement("NO_CRYPTO");
//		model.addElement("SSL");
//		model.addElement("DH_3DES");
//		model.addElement("RSA");
//		model.addElement("INTEGRITY");
		cbCyptoMode = new JComboBox<String>(model);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		panel.add(cbCyptoMode, cs);
		panel.setBorder(new LineBorder(Color.GRAY));


		btnLogin = new JButton("Login");

		btnLogin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tfUsername.setText("Client");
				dispose();
			}
		});
		JPanel bp = new JPanel();
		bp.add(btnLogin);
		bp.add(btnCancel);



		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(bp, BorderLayout.PAGE_END);

		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
	}

	public String getUsername() {
		return tfUsername.getText().trim();
	}

//	public String getPassword() {
//		return new String(pfPassword.getPassword());
//	}
	
	public String getCryptoMode() {
		return new String((String) cbCyptoMode.getSelectedItem());
	}

	public boolean isSucceeded() {
		return succeeded;
	}

}
