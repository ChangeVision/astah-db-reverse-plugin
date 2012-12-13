package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.util.prefs.Preferences;

import javax.swing.JTextField;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.DBProperties;
import com.change_vision.astah.extension.plugin.dbreverse.util.DatabaseTypes;
import com.change_vision.astah.extension.plugin.dbreverse.util.ReversePreferences;

public class JDBCDriverPathTextField extends JTextField {

	private static final long serialVersionUID = 2564014998996440231L;

	private static JDBCDriverPathTextField instance = null;

	public JDBCDriverPathTextField() {
		Preferences prefs = ReversePreferences.getInstance();
		String oldPath = prefs.get(DBProperties.DRIVER_PATH, "");
		if (null != oldPath) {
			setText(oldPath);
		} else {
			setText(DBProperties.getInstance().getDriverPath(DatabaseTypes.ORACLE.getType()));
		}
	}

	public static JDBCDriverPathTextField getInstance() {
		if (instance == null) {
			instance = new JDBCDriverPathTextField();
		}

		return instance;
	}
}