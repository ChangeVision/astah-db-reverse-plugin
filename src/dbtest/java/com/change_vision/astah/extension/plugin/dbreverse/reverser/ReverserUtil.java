package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReverserUtil {

	private static final Logger logger = LoggerFactory.getLogger(ReverserUtil.class);

	private static DBReader dbReader = DBReader.getInstance();

	private Properties properties = null;

	private String schemaName = null;

	public ReverserUtil() {
	}

	public ReverserUtil(String propertyFile) throws MalformedURLException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		loadPropertyFile(propertyFile);
		ConnectionInfo connection = new ConnectionInfo();
		connection.setClassname(properties.getProperty("classname"));
		connection.setJdbcurl(properties.getProperty("jdbcurl"));
		connection.setLogin(properties.getProperty("login"));
		connection.setName(properties.getProperty("name"));
		connection.setPassword(properties.getProperty("password"));
		connection.setPathfile(properties.getProperty("pathfile"));
		dbReader.setDBType(properties.getProperty("dbtype"));
		dbReader.connect(connection);

		schemaName = properties.getProperty("schemaName");
	}

	public static DBReader getDBReader() {
		return dbReader;
	}

	public Properties getProperties() {
		return properties;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void loadPropertyFile(String fileName) {
		this.properties = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = PostgreSQLReaderDBTest.class.getResourceAsStream(fileName);
			if (inputStream == null) {
				logger.error(fileName.concat(" not found."));
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
}