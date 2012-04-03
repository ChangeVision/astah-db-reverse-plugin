package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLServerReaderDBTest extends ReverserUtil {

	private Properties properties = null;

	@AfterClass
	public static void tearDown() throws SQLException {
		getDBReader().close();
	}

	private static final Logger logger = LoggerFactory.getLogger(SQLServerReaderDBTest.class);

	public SQLServerReaderDBTest() {
		loadPropertyFile("sqlserver.properties");
		properties = getProperties();
	}

	@Test
	public final void Windows統合認証で接続する() {
		ConnectionInfo connection = new ConnectionInfo();
		connection.setClassname(properties.getProperty("classname"));
		connection.setJdbcurl(properties.getProperty("jdbcurl.windowsauth"));
		connection.setLogin(properties.getProperty("login.windowsauth"));
		connection.setName(properties.getProperty("name"));
		connection.setPassword(properties.getProperty("password.windowsauth"));
		connection.setPathfile(properties.getProperty("pathfile"));
		getDBReader().setDBType(properties.getProperty("dbtype"));

		// 接続
		try {
			getDBReader().connect(connection);
		} catch (MalformedURLException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (SQLException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (InstantiationException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		}

		// 切断
		try {
			getDBReader().close();
		} catch (SQLException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		}

		// 再接続
		try {
			getDBReader().connect(connection);
		} catch (MalformedURLException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (SQLException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (InstantiationException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		}
	}

	@Test
	public final void SQLServer認証で接続する() {
		ConnectionInfo connection = new ConnectionInfo();
		connection.setClassname(properties.getProperty("classname"));
		connection.setJdbcurl(properties.getProperty("jdbcurl.sqlserverauth"));
		connection.setLogin(properties.getProperty("login.sqlserverauth"));
		connection.setName(properties.getProperty("name"));
		connection.setPassword(properties.getProperty("password.sqlserverauth"));
		connection.setPathfile(properties.getProperty("pathfile"));
		getDBReader().setDBType(properties.getProperty("dbtype"));

		// 接続
		try {
			getDBReader().connect(connection);
		} catch (MalformedURLException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (SQLException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (InstantiationException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		}

		// 切断
		try {
			getDBReader().close();
		} catch (SQLException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		}

		// 再接続
		try {
			getDBReader().connect(connection);
		} catch (MalformedURLException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (SQLException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (InstantiationException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		} catch (ClassNotFoundException e) {
			logger.debug(e.getMessage(), e);
			fail(e.getMessage());
		}
	}
}