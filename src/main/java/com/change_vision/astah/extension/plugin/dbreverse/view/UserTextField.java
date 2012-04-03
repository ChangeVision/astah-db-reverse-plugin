package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.util.prefs.Preferences;

import javax.swing.JTextField;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.DBProperties;
import com.change_vision.astah.extension.plugin.dbreverse.util.Constants;
import com.change_vision.astah.extension.plugin.dbreverse.util.ReversePreferences;

public class UserTextField extends JTextField {

	private static final long serialVersionUID = 2541589266597711793L;

	private static UserTextField instance = null;

	public UserTextField() {
		Preferences prefs = ReversePreferences.getInstance();
		String oldUser = prefs.get(DBProperties.USER, "");
		if (null != oldUser && !"".equals(oldUser)) {
			setText(oldUser);
		} else {
			setText(DBProperties.getInstance().getUser(Constants.ORACLE));
		}
	}

	public static UserTextField getInstance() {
		if (instance == null) {
			instance = new UserTextField();
		}

		return instance;
	}
}