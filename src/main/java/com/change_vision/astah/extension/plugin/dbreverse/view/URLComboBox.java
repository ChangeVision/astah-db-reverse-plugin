package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.util.prefs.Preferences;

import javax.swing.JComboBox;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.DBProperties;
import com.change_vision.astah.extension.plugin.dbreverse.util.Constants;
import com.change_vision.astah.extension.plugin.dbreverse.util.DBReverseUtil;
import com.change_vision.astah.extension.plugin.dbreverse.util.ReversePreferences;

public class URLComboBox extends JComboBox {

	private static final long serialVersionUID = 3012548023021505270L;

	private static URLComboBox instance = null;

	public URLComboBox() {
		super();
		setEditable(true);

		addActionListener(this);
	}

	public static URLComboBox getInstance() {
		if (instance == null) {
			instance = new URLComboBox();
			Preferences prefs = ReversePreferences.getInstance();
			String oldUrl = prefs.get(DBProperties.URL, "");
			initURLComboBox(oldUrl);
			if (null != oldUrl && !"".equals(oldUrl)) {
				instance.setSelectedItem(oldUrl);
			} else {
				String oracle = DBProperties.getInstance().getURL(Constants.ORACLE);
				instance.setSelectedItem(oracle);
			}
		}

		return instance;
	}

	public static void initURLComboBox(String oldUrl) {
		instance.removeAllItems();
		Object type = instance.getSelectedItem();
		instance.addItem(oldUrl);
		if (DBReverseUtil.isJdbcOdbcUrl(oldUrl) && type != null) {
			instance.addItem(DBProperties.getInstance().getURL(type.toString()));
		} else {
			instance.addItem(DBProperties.JDBC_ODBC_DATASOURCE_NAME);
		}
	}
}