package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;

public class ClearButton extends JButton implements ActionListener {

	private static final long serialVersionUID = -2288974661087802971L;

	private static ClearButton instance = null;

	public ClearButton() {
		setText(Messages.getMessage("button.text.clear"));

		addActionListener(this);
	}

	public static ClearButton getInstance() {
		if (instance == null) {
			instance = new ClearButton();
		}

		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		MessageTextArea.getInstance().setText("");
	}
}