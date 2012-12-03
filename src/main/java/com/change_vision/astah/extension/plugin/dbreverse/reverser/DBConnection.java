package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ConnectionInfo;

public class DBConnection {

	/**
	 * SCHEM_NAME String => schema name
	 */
	public static final int SCHEM_NAME = 1;

	/**
	 * TABLE_NAME String => table name
	 */
	public static final int TABLE_NAME = 3;

	/**
	 * TABLE_TYPE String => table name
	 */
	public static final int TABLE_TYPE = 4;

	private Connection connection;

	private DatabaseMetaData metaData;

	public DBConnection() {
		connection = null;
		metaData = null;
	}

	/**
	 * Establish connection with the server
	 *
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void connect(ConnectionInfo connectionInfo) throws SQLException,
			MalformedURLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		URL driverURL = new URL("file:" + connectionInfo.getPathfile().trim());
		URL[] drivers = new URL[1];
		drivers[0] = driverURL;
		ClassLoader driverClassLoader =
			new URLClassLoader(drivers, getClass().getClassLoader());
		Driver driver = (Driver) Class.forName(connectionInfo.getClassname(), true,
				driverClassLoader).newInstance();
		Properties p = new Properties();
		p.put("user", connectionInfo.getLogin());
		p.put("password", connectionInfo.getPassword());
		connection = driver.connect(connectionInfo.getJdbcurl(), p);
		metaData = connection.getMetaData();
	}

	public PreparedStatement getpreparedSql(String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}

	public String[] getCatalogs() throws SQLException {
		Vector<String> vCatalog = new Vector<String>();
		ResultSet res = metaData.getCatalogs();
		while (res.next()) {
			String name = res.getString(SCHEM_NAME);
			if (!"".equals(name) && !vCatalog.contains(name)) {
				vCatalog.add(name);
			}
		}
		res.close();

		String catalog[] = new String[vCatalog.size()];
		vCatalog.copyInto(catalog);
		return catalog;
	}

	public String[] getSchemas() throws SQLException {
		Vector<String> vSchema = new Vector<String>();
		ResultSet res = metaData.getSchemas();
		while (res.next()) {
			String name = res.getString(SCHEM_NAME);
			if (!"".equals(name) && !vSchema.contains(name)) {
				vSchema.add(name);
			}
		}
		res.close();

		String schema[] = new String[vSchema.size()];
		vSchema.copyInto(schema);
		return schema;
	}

	public List<String> getTables(String catalog, String schema) throws SQLException {
		List<String> tables = new ArrayList<String>();
		metaData = connection.getMetaData();
		String[] names = {"TABLE", "SYSTEM TABLE"};
		ResultSet res = metaData.getTables(catalog, schema, "%", names);
		while (res.next()) {
			tables.add(res.getString(TABLE_NAME));
		}
		res.close();

		return tables;
	}

	public List<String> getTablesFromHiRDB(String catalog, String schema) throws SQLException {
		List<String> tables = new ArrayList<String>();
		metaData = connection.getMetaData();
		String[] names = {"BASE TABLE", "SYSTEM TABLE"};
		ResultSet res = metaData.getTables(catalog, schema, "%", names);
		while (res.next()) {
			tables.add(res.getString(TABLE_NAME));
		}
		res.close();

		return tables;
	}

	/**
	 * FIXME Maybe this method is not used by other methods.
	 */
	public ResultSet getAttributes(String catalog, String schema,String table) throws SQLException {
		return metaData.getColumns(catalog, schema, table, "%");
	}

	/**
	 * Close connection
	 */
	public void close() throws SQLException {
		if (connection != null) {
			connection.close();
		}
		connection = null;
		metaData = null;
	}

	public DatabaseMetaData getMetaData() {
		return metaData;
	}
}