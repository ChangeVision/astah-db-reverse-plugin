package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.util.prefs.Preferences;

import javax.swing.JTextField;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.DBProperties;
import com.change_vision.astah.extension.plugin.dbreverse.util.Constants;
import com.change_vision.astah.extension.plugin.dbreverse.util.ReversePreferences;

public class JDBCDriverURLTextField extends JTextField {

	private static final long serialVersionUID = 2907080018266361620L;

	private static JDBCDriverURLTextField instance = null;

	public JDBCDriverURLTextField() {
		Preferences prefs = ReversePreferences.getInstance();
		String oldDriver = prefs.get(DBProperties.JDBC_DRIVER, "");
		if (null != oldDriver && !"".equals(oldDriver)) {
			setText(oldDriver);
		} else {
			setText(DBProperties.getInstance().getJDBCDriver(Constants.ORACLE));
		}
	}

	public static JDBCDriverURLTextField getInstance() {
		if (instance == null) {
			instance = new JDBCDriverURLTextField();
		}

		return instance;
	}
}