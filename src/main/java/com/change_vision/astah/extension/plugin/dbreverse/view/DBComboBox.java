package com.change_vision.astah.extension.plugin.dbreverse.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

import javax.swing.JComboBox;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.DBProperties;
import com.change_vision.astah.extension.plugin.dbreverse.util.DBReverseUtil;
import com.change_vision.astah.extension.plugin.dbreverse.util.ReversePreferences;

public class DBComboBox extends JComboBox implements ActionListener {

	private static final long serialVersionUID = 5263785013028905024L;

	private static DBComboBox instance = null;

	public DBComboBox() {
		setEditable(false);

		addItems();

		Preferences prefs = ReversePreferences.getInstance();
		String selectedCurrentDB = prefs.get(DBProperties.CURRENT_DB, "");
		setSelectedItem(selectedCurrentDB);

		addActionListener(this);
	}

	public static DBComboBox getInstance() {
		if (instance == null) {
			instance = new DBComboBox();
		}

		return instance;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DBReverseUtil.updateConntectInfo(null);
		if (!DBReverseUtil.shouldDBConnected()) {
			DBReverseUtil.disconnectDB();
		}
	}

	private void addItems() {
		for (String dbName : DBProperties.getInstance().getDBTypes()) {
			addItem(dbName.trim());
		}
	}
}