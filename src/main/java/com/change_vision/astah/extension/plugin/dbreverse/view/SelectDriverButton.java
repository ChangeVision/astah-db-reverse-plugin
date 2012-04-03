package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;

public class SelectDriverButton extends JButton implements ActionListener {

	private static final long serialVersionUID = -7196201194575977138L;

	private static SelectDriverButton instance = null;

	public SelectDriverButton() {
		setText(Messages.getMessage("button.text.select"));

		addActionListener(this);
	}

	public static SelectDriverButton getInstance() {
		if (instance == null) {
			instance = new SelectDriverButton();
		}

		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SelectJDBCDriverFileChooser fileChooser = new SelectJDBCDriverFileChooser();
		if (fileChooser != null
				&& fileChooser.getFilePath() != null) {
			JDBCDriverPathTextField.getInstance().setText(fileChooser.getFilePath());
		}
	}
}