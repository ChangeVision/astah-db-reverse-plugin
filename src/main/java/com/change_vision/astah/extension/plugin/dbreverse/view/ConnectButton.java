package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;
import com.change_vision.astah.extension.plugin.dbreverse.util.DBReverseUtil;

public class ConnectButton extends JButton implements ActionListener {

	private static final long serialVersionUID = -6323823860552231451L;

	private static ConnectButton instance = null;

	public ConnectButton() {
		setText(Messages.getMessage("button.text.connect"));
		addActionListener(this);
	}

	public static ConnectButton getInstance() {
		if (instance == null) {
			instance = new ConnectButton();
		}

		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (DBReverseUtil.shouldDBConnected()) {
			DBReverseUtil.connect();
			DBReverseUtil.saveConnectInfo();
		} else {
			DBReverseUtil.disconnectDB();
		}
	}
}