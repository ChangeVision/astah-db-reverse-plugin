package com.change_vision.astah.extension.plugin.dbreverse.view;

import javax.swing.JComboBox;

public class SchemaComboBox extends JComboBox {

	private static final long serialVersionUID = -8568622052791796459L;

	private static SchemaComboBox instance = null;

	public SchemaComboBox() {
		setEditable(false);
		setEnabled(false);
	}

	public static SchemaComboBox getInstance() {
		if (instance == null) {
			instance = new SchemaComboBox();
		}

		return instance;
	}
}