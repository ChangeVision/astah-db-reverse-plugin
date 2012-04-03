package com.change_vision.astah.extension.plugin.dbreverse.view;

import javax.swing.JPasswordField;

public class PasswordField extends JPasswordField {

	private static final long serialVersionUID = 2740874280851518951L;

	private static PasswordField instance = null;

	public PasswordField() {
	}

	public static PasswordField getInstance() {
		if (instance == null) {
			instance = new PasswordField();
		}

		return instance;
	}
}