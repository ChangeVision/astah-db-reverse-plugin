package com.change_vision.astah.extension.plugin.dbreverse.util;

import org.junit.Test;

import com.change_vision.astah.extension.plugin.dbreverse.util.Constants;
import com.change_vision.astah.extension.plugin.dbreverse.util.DBReverseUtil;
import com.change_vision.astah.extension.plugin.dbreverse.util.ReversePreferences;
import com.change_vision.astah.extension.plugin.dbreverse.view.DBComboBox;
import com.change_vision.astah.extension.plugin.dbreverse.view.JDBCDriverPathTextField;
import com.change_vision.astah.extension.plugin.dbreverse.view.JDBCDriverURLTextField;
import com.change_vision.astah.extension.plugin.dbreverse.view.PasswordField;
import com.change_vision.astah.extension.plugin.dbreverse.view.ReverseDialog;
import com.change_vision.astah.extension.plugin.dbreverse.view.SchemaComboBox;
import com.change_vision.astah.extension.plugin.dbreverse.view.URLComboBox;
import com.change_vision.astah.extension.plugin.dbreverse.view.UserTextField;

public class DBReverseUtilDBTest {

	@Test
	public final void testCloseReader() {
		DBReverseUtil.closeReader();

		ReversePreferences.getInstace(ReverseDialog.class);
		DBComboBox dbComboBox = DBComboBox.getInstance();
		URLComboBox urlComboBox = URLComboBox.getInstance();
		UserTextField userTextField = UserTextField.getInstance();
		PasswordField passwordField = PasswordField.getInstance();
		JDBCDriverPathTextField driverPath = JDBCDriverPathTextField.getInstance();
		JDBCDriverURLTextField driverUrl = JDBCDriverURLTextField.getInstance();

		dbComboBox.setSelectedItem(Constants.POSTGRES);
		urlComboBox.setSelectedItem("jdbc:postgres://127.0.0.1:5432/postgres");
		userTextField.setText("postgres");
		passwordField.setText("postgres");
		driverPath.setText("c:\\workspace\\postgresql-9.0-801.jdbc4.jar");
		driverUrl.setText("org.postgresql.Driver");

		DBReverseUtil.connect();

		SchemaComboBox.getInstance().setSelectedItem("test");

		DBReverseUtil.closeReader();
	}
}