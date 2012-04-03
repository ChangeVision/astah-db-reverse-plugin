package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBProperties {

	private static final Logger logger = LoggerFactory.getLogger(DBProperties.class);

	private static final String FILE_NAME = "Config.properties";

	public static final String POINT = ".";

	public static final String URL = "URL";

	public static final String USER = "User";

	public static final String JDBC_DRIVER = "JDBC_Driver";

	public static final String DRIVER_PATH = "Driver_Path";

	public static final String CURRENT_DB = "Current_DB";

	public static final String DB_TYPES = "DB_Types";

	public static final String JDBC_ODBC = "jdbc:odbc:";

	public static final String JDBC_ODBC_DATASOURCE_NAME = "jdbc:odbc:dataSoureceName";

	public static final String SUN_JDBC_ODBC_DRIVER = "sun.jdbc.odbc.JdbcOdbcDriver";

	private static DBProperties instance = null;

	private Properties properties;

	public DBProperties() {
		load();
	}

	public static DBProperties getInstance() {
		if (instance == null) {
			instance = new DBProperties();
		}

		return instance;
	}

	private void load() {
		this.properties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = DBProperties.class.getResourceAsStream(FILE_NAME);
			if (inputStream == null) {
				logger.error(FILE_NAME.concat(" not found."));
			}
			this.properties.load(inputStream);
			inputStream.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	public String[] getDBTypes() {
		String types = getString(DB_TYPES);
		return types.split(",");
	}

	public String getCurrentDB() {
		return getString(CURRENT_DB);
	}

	public void setCurrentDB(String value) {
		setString(CURRENT_DB, value);
	}

	public String getURL(String dbType) {
		return getString(dbType + POINT + URL);
	}

	public void setURL(String dbType, String value) {
		if (value != null && !value.equals(getURL(dbType))) {
			setString(dbType + POINT + URL, value);
		}
	}

	public String getUser(String dbType) {
		return getString(dbType + POINT + USER);
	}

	public void setUser(String dbType, String value) {
		if (value != null && !value.equals(getUser(dbType))) {
			setString(dbType + POINT + USER, value);
		}
	}

	public String getJDBCDriver(String dbType) {
		return getString(dbType + POINT + JDBC_DRIVER);
	}

	public void setJDBCDriver(String dbType, String value) {
		if (value != null && !value.equals(getJDBCDriver(dbType))) {
			setString(dbType + POINT + JDBC_DRIVER, value);
		}
	}

	public String getDriverPath(String dbType) {
		return getString(dbType + POINT + DRIVER_PATH);
	}

	public void setDriverPath(String dbType, String value) {
		if (value != null && !value.equals(getDriverPath(dbType))) {
			setString(dbType + POINT + DRIVER_PATH, value);
		}
	}

	private String getString(String key) {
		return this.properties.getProperty(key);
	}

	private void setString(String key, String value) {
		if (value == null) {
			return;
		}
		this.properties.setProperty(key, value);
	}
}