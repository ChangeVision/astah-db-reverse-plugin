package com.change_vision.astah.extension.plugin.dbreverse.util;

import java.awt.Component;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.dbreverse.Messages;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.ConnectionInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.DBProperties;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.DBReader;
import com.change_vision.astah.extension.plugin.dbreverse.view.ConnectButton;
import com.change_vision.astah.extension.plugin.dbreverse.view.DBComboBox;
import com.change_vision.astah.extension.plugin.dbreverse.view.ImportButton;
import com.change_vision.astah.extension.plugin.dbreverse.view.JDBCDriverPathTextField;
import com.change_vision.astah.extension.plugin.dbreverse.view.JDBCDriverURLTextField;
import com.change_vision.astah.extension.plugin.dbreverse.view.MessageTextArea;
import com.change_vision.astah.extension.plugin.dbreverse.view.PasswordField;
import com.change_vision.astah.extension.plugin.dbreverse.view.SchemaComboBox;
import com.change_vision.astah.extension.plugin.dbreverse.view.SelectDriverButton;
import com.change_vision.astah.extension.plugin.dbreverse.view.URLComboBox;
import com.change_vision.astah.extension.plugin.dbreverse.view.UserTextField;
import com.change_vision.jude.api.inf.exception.InvalidEditingException;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;
import com.change_vision.jude.api.inf.view.IViewManager;

public class DBReverseUtil {

	private static final Logger logger = LoggerFactory.getLogger(DBReverseUtil.class);

	public static JFrame getMainFrame() {
		JFrame parent = null;
		try {
			ProjectAccessor projectAccessor = ProjectAccessorFactory.getProjectAccessor();
			if (projectAccessor == null) {
				return null;
			}
			IViewManager viewManager = projectAccessor.getViewManager();
			if (viewManager == null) {
				return null;
			}
			parent = viewManager.getMainFrame();
		} catch (ClassNotFoundException ex) {
			return null;
		} catch (InvalidUsingException e) {
			return null;
		}
		return parent;
	}

	public static void updateConntectInfo(String url) {
		String dbType = DBComboBox.getInstance().getSelectedItem().toString();
		Preferences prefs = ReversePreferences.getInstance();
		if (dbType.equals(prefs.get(DBProperties.CURRENT_DB, ""))
				&& (url == null || url.equals(prefs.get(DBProperties.URL, "")))) {
			String oldUrl = prefs.get(DBProperties.URL, "");
			URLComboBox.initURLComboBox(oldUrl);
			URLComboBox.getInstance().setSelectedItem(prefs.get(DBProperties.URL, ""));
			UserTextField.getInstance().setText(prefs.get(DBProperties.USER, ""));
			JDBCDriverURLTextField.getInstance().setText(prefs.get(DBProperties.JDBC_DRIVER, ""));
			JDBCDriverPathTextField.getInstance().setText(prefs.get(DBProperties.DRIVER_PATH, ""));
		} else {
			if (url == null) {
				url = DBProperties.getInstance().getURL(dbType);
				URLComboBox.initURLComboBox(url);
				URLComboBox.getInstance().setSelectedItem(url);
			}
			if (DBReverseUtil.isJdbcOdbcUrl(url)) {
				UserTextField.getInstance().setText(DBProperties.getInstance().getUser(dbType));
				JDBCDriverURLTextField.getInstance().setText(DBProperties.SUN_JDBC_ODBC_DRIVER);
				JDBCDriverPathTextField.getInstance().setText("");
			} else {
				UserTextField.getInstance().setText(DBProperties.getInstance().getUser(dbType));
				JDBCDriverURLTextField.getInstance().setText(DBProperties.getInstance().getJDBCDriver(dbType));
				JDBCDriverPathTextField.getInstance().setText(DBProperties.getInstance().getDriverPath(dbType));
			}
		}
		PasswordField.getInstance().setText("");
	}

	public static boolean shouldDBConnected() {
		ConnectButton button = ConnectButton.getInstance();
		if (button == null) {
			return false;
		}

		return Messages.getMessage("button.text.connect").equals(button.getText());
	}

	public static void connect() {
		SchemaComboBox schemaCombo = SchemaComboBox.getInstance();
		schemaCombo.removeAllItems();
		DBReader dbReader = DBReader.getInstance();

		String dbType = DBComboBox.getInstance().getSelectedItem().toString();
		if (Constants.ORACLE.equalsIgnoreCase(dbType)) {
			dbReader.setDBType(DBReader.ORACLE);
		} else if (Constants.MYSQL.equalsIgnoreCase(dbType)) {
			dbReader.setDBType(DBReader.MYSQL);
		} else if (Constants.MSSQLSERVER.equalsIgnoreCase(dbType)) {
			dbReader.setDBType(DBReader.MSSQLSERVER);
		} else if (Constants.POSTGRES.equalsIgnoreCase(dbType)) {
			dbReader.setDBType(DBReader.POSTGRES);
		} else if (Constants.HSQL.equalsIgnoreCase(dbType)) {
			dbReader.setDBType(DBReader.HSQL);
		} else if (Constants.H2.equalsIgnoreCase(dbType)) {
			dbReader.setDBType(DBReader.H2);
		} else if (Constants.HiRDB.equalsIgnoreCase(dbType)) {
			dbReader.setDBType(DBReader.HiRDB);
		} else {
			dbReader.setDBType(dbType);
		}

		ConnectionInfo diverInfo = new ConnectionInfo();
		diverInfo.setClassname(JDBCDriverURLTextField.getInstance().getText());
		diverInfo.setPathfile(JDBCDriverPathTextField.getInstance().getText());

		diverInfo.setLogin(UserTextField.getInstance().getText());
		diverInfo.setPassword(String.copyValueOf(PasswordField.getInstance().getPassword()));

		String selectedURL = URLComboBox.getInstance().getSelectedItem().toString();
		diverInfo.setJdbcurl(selectedURL);

		try {
			dbReader.connect(diverInfo);
			ImportButton.getInstance().setEnabled(true);
			schemaCombo.setEnabled(true);

			String selectURL = selectedURL.toLowerCase();
			for (String schema : dbReader.getSchemas()) {
				schemaCombo.addItem(schema);
				if (selectURL.endsWith(schema.toLowerCase())) {
					schemaCombo.setSelectedItem(schema);
				}
			}

			showMessage(Messages.getMessage("message.connection.succeeded"));

			postConnect();

		} catch (MalformedURLException e) {
			logger.error(e.getMessage(), e);
			dbReader = null;
			showErrorMessage(e);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			dbReader = null;
			showErrorMessage(e);
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
			dbReader = null;
			showErrorMessage(e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			dbReader = null;
			showErrorMessage(e);
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
			dbReader = null;
			showErrorMessage(e);
		}
	}

	private static void postConnect() {
		ConnectButton.getInstance().setText(Messages.getMessage("button.text.disconnect"));

		DBComboBox.getInstance().setEnabled(false);
		URLComboBox.getInstance().setEnabled(false);
		UserTextField.getInstance().setEditable(false);
		PasswordField.getInstance().setEditable(false);
		JDBCDriverURLTextField.getInstance().setEditable(false);
		SelectDriverButton.getInstance().setEnabled(false);
		JDBCDriverPathTextField.getInstance().setEditable(false);
	}

	public static void saveConnectInfo() {
		String dbType = DBComboBox.getInstance().getSelectedItem().toString();
		Preferences prefs = ReversePreferences.getInstance();
		try {
			prefs.put(DBProperties.CURRENT_DB, dbType);
			prefs.put(DBProperties.URL, URLComboBox.getInstance().getSelectedItem().toString());
			prefs.put(DBProperties.USER, UserTextField.getInstance().getText());
			prefs.put(DBProperties.JDBC_DRIVER, JDBCDriverURLTextField.getInstance().getText());
			prefs.put(DBProperties.DRIVER_PATH, JDBCDriverPathTextField.getInstance().getText());
			prefs.flush();
		} catch (BackingStoreException e) {
			logger.error(e.getMessage(), e);
		}
	}

	public static void disconnectDB() {
		closeReader();

		ConnectButton.getInstance().setText(Messages.getMessage("button.text.connect"));

		DBComboBox.getInstance().setEnabled(true);
		URLComboBox.getInstance().setEnabled(true);
		UserTextField.getInstance().setEditable(true);
		PasswordField.getInstance().setEditable(true);
		JDBCDriverURLTextField.getInstance().setEditable(true);
		SelectDriverButton.getInstance().setEnabled(true);
		JDBCDriverPathTextField.getInstance().setEditable(true);
		SchemaComboBox.getInstance().removeAllItems();
		SchemaComboBox.getInstance().setEnabled(false);

		ImportButton.getInstance().setEnabled(false);
	}

	public static void closeReader() {
		DBReader dbReader = DBReader.getInstance();
		if (dbReader != null) {
			try {
				dbReader.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
				showErrorMessage(e);
			}
			dbReader = null;
			showMessage(Messages.getMessage("message.connection.closed"));
		}
	}

	public static void showMessage(String message) {
		if (!message.endsWith("\n")) {
			MessageTextArea.getInstance().append(message + "\n");
		} else {
			MessageTextArea.getInstance().append(message);
		}
	}

	public static void showErrorMessage(Throwable e) {
		if (e instanceof InvalidEditingException) {
			InvalidEditingException exception = (InvalidEditingException) e;
			MessageTextArea.getInstance().append(exception.getKey() + " : " + exception.getMessage() + "\n");
		} else {
			MessageTextArea.getInstance().append(e.toString() + "\n");
		}
	}

	public static boolean isJdbcOdbcUrl(String url) {
		return url != null
		&& url.length() >= 10
		&& url.substring(0,10).equalsIgnoreCase(DBProperties.JDBC_ODBC);
	}

	public static void showInformationDialog(Component parentComponent, String message) {
		JOptionPane.showMessageDialog(parentComponent, message, Messages.getMessage("dialog.title.infomation"), JOptionPane.INFORMATION_MESSAGE);
	}
}