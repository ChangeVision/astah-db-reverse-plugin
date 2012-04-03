package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;
import com.change_vision.astah.extension.plugin.dbreverse.util.DBReverseUtil;

public class CloseButton extends JButton implements ActionListener {

	private static final long serialVersionUID = 5488998733416818192L;

	private static CloseButton instance = null;

	public CloseButton() {
		setText(Messages.getMessage("button.text.close"));

		addActionListener(this);
	}

	public static CloseButton getInstance() {
		if (instance == null) {
			instance = new CloseButton();
		}

		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DBReverseUtil.disconnectDB();
		ReverseDialog.getInstance().dispose();
	}
}