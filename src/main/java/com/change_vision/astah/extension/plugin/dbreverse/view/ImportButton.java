package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.ImportToProject;

public class ImportButton extends JButton implements ActionListener {

	private static final long serialVersionUID = -8333380826691409178L;

	private static final String NAME = "button.import";

	private static ImportButton instance = null;

	public ImportButton() {
		setName(NAME);
		setText(Messages.getMessage("button.text.import"));
		setEnabled(false);

		addActionListener(this);
	}

	public static ImportButton getInstance() {
		if (instance == null) {
			instance = new ImportButton();
		}

		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setEnabled(false);
		SchemaComboBox.getInstance().setEnabled(false);
		ImportToProject.doImport();
	}
}