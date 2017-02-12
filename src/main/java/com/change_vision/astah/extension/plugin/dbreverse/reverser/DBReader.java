package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.AttributeInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ConnectionInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DatatypeInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ERRelationshipInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.IndexInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.TableInfo;
import com.change_vision.astah.extension.plugin.dbreverse.util.DatabaseTypes;

public class DBReader {

	/**
	 * TABLE_CAT String => table catalog (may be null)
	 */
	public static final int TABLE_CAT = 1;

	/**
	 * TABLE_SCHEM String => table schema (may be null)
	 */
	public static final int TABLE_SCHEM = 2;

	/**
	 * TABLE_NAME String => table name
	 */
	public static final int TABLE_NAME = 3;

	/**
	 * COLUMN_NAME String => column name
	 */
	public static final int COLUMN_NAME = 4;

	/**
	 * NON_UNIQUE boolean => unique
	 */
	public static final int NON_UNIQUE = 4;

	/**
	 * DATA_TYPE int => SQL type from java.sql.Types
	 */
	public static final int DATA_TYPE = 5;

	/**
	 * TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
	 */
	public static final int TYPE_NAME = 6;

	/**
	 * INDEX_NAME String => index name
	 */
	public static final int INDEX_NAME = 6;

	/**
	 * COLUMN_SIZE int => column size.
	 * For char or date types this is the maximum number of characters, for numeric or decimal types this is precision.
	 */
	public static final int COLUMN_SIZE = 7;

	/**
	 * FOREIGN_KEY string => foreign key
	 */
	public static final int FOREIGN_KEY = 8;

	/**
	 * DECIMAL_DIGITS int => the number of fractional digits
	 */
	public static final int DECIMAL_DIGITS = 9;

	/**
	 * INDEX_COLUMNS String => columns for indexes
	 */
	public static final int INDEX_COLUMNS = 9;

	/**
	 * NUM_PREC_RADIX int => Radix (typically either 10 or 2)
	 */
	public static final int NUM_PREC_RADIX = 10;

	/**
	 * NULLABLE int => is NULL allowed.
	 * columnNoNulls - might not allow NULL values
	 * columnNullable - definitely allows NULL values
	 * columnNullableUnknown - nullability unknown
	 */
	public static final int NULLABLE = 11;

	/**
	 * REMARKS String => comment describing column (may be null)
	 */
	public static final int REMARKS = 12;

	/**
	 * RELATION_NAME String => relation name
	 */
	public static final int RELATION_NAME = 12;

	/**
	 * COLUMN_DEF String => default value (may be null)
	 */
	public static final int COLUMN_DEF = 13;

	/**
	 * SQL_DATA_TYPE int => unused
	 */
	public static final int SQL_DATA_TYPE = 14;

	/**
	 * SQL_DATETIME_SUB int => unused
	 */
	public static final int SQL_DATETIME_SUB = 15;

	/**
	 * CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
	 */
	public static final int CHAR_OCTET_LENGTH = 16;

	/**
	 * int => index of column in table (starting at 1)
	 */
	public static final int ORDINAL_POSITION = 17;

	/**
	 * String => "NO" means column definitely does not allow NULL values; "YES" means the column might allow NULL values. An empty string means nobody knows.
	 */
	public static final int IS_NULLABLE = 18;

	/**
	 * String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
	 */
	public static final int SCOPE_CATLOG = 19;

	/**
	 * String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
	 */
	public static final int SCOPE_SCHEMA = 20;

	/**
	 * String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF)
	 */
	public static final int SCOPE_TABLE = 21;

	/**
	 * short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
	 */
	public static final int SOURCE_DATA_TYPE = 22;

	public static final String NONE = "None";

	public static final String OPTIONAL = "Optional";

	public static final String REQUIRED = "Required";

	private static final Logger logger = LoggerFactory.getLogger(DBReader.class);

	private static DBReader instace = null;

	private DBConnection connection;

	private String dbType;

	public DBReader() {
		connection = null;
		dbType = "";
	}

	public static DBReader getInstance() {
		if (instace == null) {
			instace = new DBReader();
		}

		return instace;
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
		close();
		connection = new DBConnection();
		connection.connect(connectionInfo);
	}

	public void close() throws SQLException {
		if (connection != null) {
			connection.close();
			connection = null;
		}
	}

	public String[] getSchemata() throws SQLException {
		if (DatabaseTypes.ORACLE.selected(dbType)) {
			return connection.getSchemata();
		} else if (DatabaseTypes.MYSQL.selected(dbType)) {
			return connection.getCatalogs();
		} else if (DatabaseTypes.MSSQLSERVER.selected(dbType)) {
			return connection.getCatalogs();
		} else if (DatabaseTypes.POSTGRES.selected(dbType)) {
			return connection.getSchemata();
		} else if (DatabaseTypes.H2.selected(dbType)) {
			return connection.getSchemata();
		} else if (DatabaseTypes.HSQL.selected(dbType)) {
			return connection.getSchemata();
		} else if (DatabaseTypes.HiRDB.selected(dbType)) {
			return connection.getSchemata();
		} else if (DatabaseTypes.OTHERS_CATEGORY.selected(dbType)) {
			return connection.getCatalogs();
		} else if (DatabaseTypes.OTHERS_SCHEMA.selected(dbType)) {
			return connection.getSchemata();
		}
		return connection.getSchemata();
	}

	public HashSet<String> getPKs(TableInfo tbInfo) throws SQLException {
		ResultSet res = getPKSet(tbInfo.getCatalog(), tbInfo.getSchema(), tbInfo.getName());
		if (res == null) {
			res = getPKSet(tbInfo.getSchema(), tbInfo.getCatalog(), tbInfo.getName());
		}
		HashSet<String> pks = new HashSet<String>();
		if (res != null) {
			while (res.next()) {
				pks.add(res.getString(COLUMN_NAME));
			}
			res.close();
		}
		return pks;
	}

	private ResultSet getPKSet(String catalog, String schema, String table) {
		try {
			return connection.getMetaData().getPrimaryKeys(catalog, schema, table);
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public HashMap<String, String> getFKs(TableInfo tbInfo) throws SQLException {
		HashMap<String, String> fks = new HashMap<String, String>();
		ResultSet res = null;
		try {
			res = connection.getMetaData().getImportedKeys(tbInfo.getCatalog(), tbInfo.getSchema(), tbInfo.getName());
		} catch (Exception e) {
			return fks;
		}
		while (res.next()) {
			// reference table name
			String tableName = res.getString(TABLE_NAME);
			// reference column name(primary key)
			String pkName = res.getString(COLUMN_NAME);
			// current column name(foreign key)
			String fkName = res.getString(FOREIGN_KEY);

			fks.put(fkName, tableName + "," + pkName);
		}
		res.close();
		return fks;
	}

	public List<TableInfo> getTables(String catalog, String schema) throws SQLException {
		if (connection == null) {
			return null;
		}
		
        List<TableInfo> tableList;
		if (DatabaseTypes.HiRDB.selected(dbType)) {
		    tableList = connection.getTablesFromHiRDB(catalog, schema);
		} else {
		    tableList = connection.getTables(catalog, schema);
		}

		for (TableInfo tbInfo : tableList) {
			// attributes
			tbInfo.addAttributes(getAttributes(tbInfo));
			// indexes
			tbInfo.setIndexes(getIndexes(tbInfo));
			
			logger.debug(tbInfo.toString());
		}

		return tableList;
	}

	private List<IndexInfo> getIndexes(TableInfo tbInfo) throws SQLException {
		List<IndexInfo> indexes = new ArrayList<IndexInfo>();
		indexes.addAll(getIndexInfoes(tbInfo));
		return indexes;
	}

	private List<IndexInfo> getIndexInfoes(TableInfo tbInfo) throws SQLException {
		List<String> uniques = getUniqueIndexes(tbInfo);
		ResultSet indexSet = getIndexSet(tbInfo.getCatalog(),tbInfo.getSchema(),tbInfo.getName(), false);
		if (indexSet == null) {
			indexSet = getIndexSet(tbInfo.getSchema(),tbInfo.getCatalog(),tbInfo.getName(), false);
		}

		List<IndexInfo> indexes = new ArrayList<IndexInfo>();
		if (indexSet != null) {
			Map<String, IndexInfo> indexMap = new HashMap<String, IndexInfo>();
			while (indexSet.next()) {
				//parentEntity
				String tableName = indexSet.getString(TABLE_NAME);
				//name
				String name = indexSet.getString(INDEX_NAME);
				if (name == null) {
					continue;
				}
				IndexInfo indexInfo;
				if (indexMap.get(name) != null) {
					indexInfo = (IndexInfo)indexMap.get(name);
				} else {
					indexInfo = new IndexInfo();
					//unique
					indexInfo.setUnique(uniques.contains(name));
					indexInfo.setName(name);
					indexInfo.setParentEntity(tableName);
				}
				//attributes
				String[] attributes = indexSet.getString(INDEX_COLUMNS).split(",");
				for (int i = 0 ; i < attributes.length; i++) {
					indexInfo.addAttribute(attributes[i]);
				}
				if (!indexes.contains(indexInfo)) {
					indexes.add(indexInfo);
				}
				indexMap.put(name, indexInfo);
			}
			indexSet.close();
		}
		return indexes;
	}

	private ResultSet getIndexSet(String catalog, String schema, String table, boolean isUnique) {
		try {
			return connection.getMetaData().getIndexInfo(catalog, schema, table, isUnique, true);
		} catch (SQLException e) {
			return null;
		}
	}

	private List<String> getUniqueIndexes(TableInfo tbInfo) throws SQLException {
		List<String> uniques = new ArrayList<String>();
		ResultSet indexSet = getIndexSet(tbInfo.getCatalog(), tbInfo.getSchema(), tbInfo.getName(), true);
		if (indexSet == null) {
			indexSet = getIndexSet(tbInfo.getSchema(), tbInfo.getCatalog(), tbInfo.getName(), true);
		}
		if (indexSet != null) {
			while (indexSet.next()) {
				//name
				String name = indexSet.getString(INDEX_NAME);
				if (name == null) {
					continue;
				}
				uniques.add(name);
			}
			indexSet.close();
		}
		return uniques;
	}

	private List<AttributeInfo> getAttributes(TableInfo tableInfo) throws SQLException {
		HashSet<String> pks = getPKs(tableInfo);
		HashMap<String, String> fks = getFKs(tableInfo);
		ResultSet attrSet = connection.getMetaData().getColumns(tableInfo.getCatalog(), tableInfo.getSchema(), tableInfo.getName(), "%");
		List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
		List<String> attrs = new ArrayList<String>();
		StringBuffer logBuf = new StringBuffer();
        logBuf.append("\nfromDB Table:" + tableInfo.getName() + "[\n");
		while (attrSet.next()) {
			AttributeInfo atInfo = new AttributeInfo();

			//name
			String attrName = getString(attrSet, COLUMN_NAME);
			if (attrs.contains(attrName)) {
				continue;
			}
			attrs.add(attrName);
			atInfo.setName(attrName);
            logBuf.append(" [attrName=" + attrName);
	        
			//data type
			DatatypeInfo dInfo = getDatatypeInfo(attrSet);
			atInfo.setDataType(dInfo);
            logBuf.append(", type=" + dInfo.getName());
            
			//length precision
			int length = getLength(attrSet, dInfo);
            logBuf.append(", length=" + length);
			if (!NONE.equals(dInfo.getPrecisionConstraint())) {
				atInfo.setPrecision(String.valueOf(length));
			} else if (!NONE.equals(dInfo.getLengthConstraint())) {
				atInfo.setLength(String.valueOf(length));
			}

			//remark
			String definition = getString(attrSet, REMARKS);
            logBuf.append(", remarks=" + definition);
			definition = definition == null ? "" : definition;
			atInfo.setDefinition(definition);

			//default value
			String defaultValue = getString(attrSet, COLUMN_DEF);
            logBuf.append(", defaultValue=" + defaultValue);            
			defaultValue = defaultValue == null ? "" : defaultValue;
			atInfo.setDefaultValue(defaultValue);

			//not null
			String notNull = getString(attrSet,IS_NULLABLE);
            if (notNull != null) {
				notNull = notNull.trim();
			}
			atInfo.setNotNull("NO".equals(notNull));
            logBuf.append(", nullable=" + notNull);
            
            logBuf.append("]\n");
            
			//Primary Key
			atInfo.setPK(pks.contains(attrName));
			atInfo.setFK(fks.keySet().contains(attrName));

			attributes.add(atInfo);
		}
		attrSet.close();
        logBuf.append("]");
        logger.debug(logBuf.toString());
		return attributes;
	}

	private String getString(ResultSet rSet, int columnIndex) {
		String value;
		try {
			value = rSet.getString(columnIndex);
		} catch (Exception e) {
			return "";
		}
		return value;
	}

	private int getLength(ResultSet attrSet, DatatypeInfo dInfo) throws SQLException {
		if (DatabaseTypes.POSTGRES.selected(dbType)
				&& ("TIME".equalsIgnoreCase(dInfo.getName())
						|| "TIMESTAMP".equalsIgnoreCase(dInfo.getName())
						|| "TIMESTAMPTZ".equalsIgnoreCase(dInfo.getName())
						|| "TIMETZ".equalsIgnoreCase(dInfo.getName())
						|| "INTERVAL".equalsIgnoreCase(dInfo.getName()))) {
			return attrSet.getInt(DECIMAL_DIGITS);
		}
		return attrSet.getInt(COLUMN_SIZE);
	}

	private DatatypeInfo getDatatypeInfo(ResultSet attrSet) throws SQLException {
		DatatypeInfo dInfo = new DatatypeInfo();
		String dtName = attrSet.getString(TYPE_NAME);
		dtName = getValidDatatypeName(dtName);
		dInfo.setName(dtName);
		if (DatabaseTypes.ORACLE.selected(dbType) || DatabaseTypes.H2.selected(dbType)) {
			if ("CHAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("VARCHAR2".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(REQUIRED);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("NCHAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("NVARCHAR2".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(REQUIRED);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("NUMBER".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(OPTIONAL);
			} else if ("DATE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("LONG".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("RAW".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(REQUIRED);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("LONG RAW".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("ROWID".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BLOB".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("CLOB".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("NCLOB".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BFILE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("UROWID".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("FLOAT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(OPTIONAL);
			} else if ("XMLTYPE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TIMESTAMP".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TIMESTAMP WITH LOCAL TIME ZONE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TIMESTAMP WITH TIME ZONE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			}
		} else if (DatabaseTypes.MYSQL.selected(dbType)) {
			if ("BIT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} if ("INT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BOOL".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BOOLEAN".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TINYINT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("SMALLINT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("MEDIUMINT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("INTEGER".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BIGINT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("FLOAT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("DOUBLE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("DECIMAL".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(OPTIONAL);
			} else if ("DATE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("DATETIME".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TIMESTAMP".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TIME".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("YEAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("CHAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("VARCHAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(OPTIONAL);
			} else if ("BINARY".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("VARBINARY".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TINYBLOB".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TINYTEXT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BLOB".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TEXT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("MEDIUMBLOB".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("MEDIUMTEXT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("LONGBLOB".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("LONGTEXT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			}
		} else if (DatabaseTypes.MSSQLSERVER.selected(dbType)) {
			if ("BIGINT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BINARY".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BIT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("CHAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("DATETIME".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("DECIMAL ".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(REQUIRED);
			} else if ("FLOAT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("IMAGE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("INT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("MONEY".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("NCHAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("NTEXT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("NUMERIC".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(OPTIONAL);
			} else if ("NVARCHAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("REAL".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("SMALLDATETIME".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("SMALLINT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("SMALLMONEY".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("SQL_VARIANT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("SYSNAME".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TEXT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TIMESTAMP".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TINYINT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("UNIQUEIDENTIFIER".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("VARBINARY".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(OPTIONAL);
			} else if ("VARCHAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(OPTIONAL);
			}
		} else if (DatabaseTypes.POSTGRES.selected(dbType)) {
			if ("ABSTIME".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("ACLITEM".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BIT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BOOL".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BOX".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BPCHAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BYTEA".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("CHAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("CID".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("CIDR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("CIRCLE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("DATE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("FLOAT4".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("FLOAT8".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("GTSVECTOR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("INET".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("INT2".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("INT2VECTOR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("INT4".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("INT8".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("INTERVAL".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("LINE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("LSEG".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("MACADDR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("MONEY".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("NAME".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("NUMERIC".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(OPTIONAL);
			} else if ("OID".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("OIDVECTOR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("PATH".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("POINT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("POLYGON".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("REFCURSOR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("REGCLASS".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("REGCONFIG".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("REGDICTIONARY".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("REGOPER".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("REGOPERATOR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("REGPROC".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("REGPROCEDURE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("REGTYPE".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("RELTIME".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("SMGR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TEXT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TID".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TIME".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TIMESTAMP".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TIMESTAMPTZ".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TIMETZ".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TINTERVAL".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TSQUERY".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TSVECTOR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("TXID_SNAPSHOT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("UNKNOWN".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("UUID".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("VARBIT".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(OPTIONAL);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("VARCHAR".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(OPTIONAL);
			} else if ("XID".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("XML".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("SERIAL".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			} else if ("BIGSERIAL".equalsIgnoreCase(dtName)) {
				dInfo.setLengthConstraint(NONE);
				dInfo.setPrecisionConstraint(NONE);
			}
		}
		return dInfo;
	}

	private String getValidDatatypeName(String name) {
		String lowerCaseName = name.toLowerCase();
		//remove "Identity", jude not support it so far
		if (lowerCaseName.indexOf("identity") != -1) {
			name = name.substring(0, lowerCaseName.indexOf("identity")).trim();
		} else if (lowerCaseName.indexOf("unsigned") != -1) {
			name = name.substring(0, lowerCaseName.indexOf("unsigned")).trim();
		}
		return name;
	}

	public List<ERRelationshipInfo> getRelationships(List<TableInfo> tbInfos) throws SQLException {
		if (connection == null) {
			return null;
		}
		List<ERRelationshipInfo> relationList = new ArrayList<ERRelationshipInfo>();
		PreparedStatement preparedStatement = null;
		for (TableInfo tbInfo : tbInfos) {
			HashSet<String> pks = getPKs(tbInfo);

			Map<String, ERRelationshipInfo> relationMap = new HashMap<String, ERRelationshipInfo>();
			ResultSet res = null;
			try {
				if (DatabaseTypes.ORACLE.selected(dbType)) {
					if (preparedStatement == null) {
						preparedStatement = getRelationinfoBySql();
					}
					preparedStatement.setObject(1, tbInfo.getName());
					res = preparedStatement.executeQuery();
				} else {
					res = connection.getMetaData().getImportedKeys(tbInfo.getCatalog(), tbInfo.getSchema(), tbInfo.getName());
				}
			} catch (Exception e) {
				continue;
			}
			while (res.next()) {
				String relationName;
				String fkName;
				String referenceTableName;
				String pkName;
				if (DatabaseTypes.ORACLE.selected(dbType)) {
					//current column name(foreign key)
					fkName = res.getString(3);
					//relation name
					relationName = res.getString(4);
					//reference table name
					referenceTableName = res.getString(6);
					//reference column name(primary key)
					pkName = res.getString(7);
				} else {
					//reference table name
					referenceTableName = res.getString(TABLE_NAME);
					//reference column name(primary key)
					pkName = res.getString(COLUMN_NAME);
					//current column name(foreign key)
					fkName = res.getString(FOREIGN_KEY);
					//relation name
					relationName = res.getString(RELATION_NAME);
				}

				String relationMapKey = referenceTableName + "_" + relationName;
				if (relationMap.keySet().contains(relationMapKey)) {
					ERRelationshipInfo info =
						(ERRelationshipInfo) relationMap.get(relationMapKey);
					if (!info.getKeys().containsKey(pkName)) {
						info.addKey(pkName, fkName);
					}
					continue;
				}

				ERRelationshipInfo rInfo = new ERRelationshipInfo();
				rInfo.setChildTable(tbInfo.getName());
				rInfo.setParentTable(referenceTableName);
				rInfo.setName(relationName);
				rInfo.addKey(pkName, fkName);

				// 一意制約、関連する親のオブジェクトが必須
				if (pks.contains(fkName)) {
					rInfo.setIdentifying(true);
					rInfo.setParentRequired(true);
				} else {
					rInfo.setNonIdentifying(true);
				}

				relationMap.put(relationMapKey, rInfo);
				relationList.add(rInfo);
	            logger.debug(rInfo.toString());
			}
			res.close();
		}
		if (preparedStatement != null) {
			preparedStatement.close();
			preparedStatement = null;
		}
		return relationList;
	}

	private PreparedStatement getRelationinfoBySql() throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("select ");
				/*FK SCHEMA*/
		sql.append("a.owner, ");
				/*FK tableName*/
		sql.append("a.table_name, ");
				/*FK name*/
		sql.append("substr(c.column_name,1,127),");
				/*Relationship Name*/
		sql.append("c.constraint_name, ");
				/*PK SCHEMA*/
		sql.append("b.owner, ");
				/*PK tableName*/
		sql.append("b.table_name, ");
				/*PK name*/
		sql.append("substr(d.column_name,1,127)");

		sql.append(" from ");
		sql.append("user_constraints a,");
		sql.append("user_constraints b,");
		sql.append("user_cons_columns c,");
		sql.append("user_cons_columns d");

		sql.append(" where ");
		sql.append("a.table_name=?");
		sql.append(" and a.r_constraint_name=b.constraint_name");
		sql.append(" and a.constraint_type='R'");
		sql.append(" and a.r_owner=b.owner");
		sql.append(" and a.constraint_name=c.constraint_name");
		sql.append(" and b.constraint_name=d.constraint_name");
		sql.append(" and a.owner=c.owner");
		sql.append(" and a.table_name=c.table_name");
		sql.append(" and b.owner=d.owner");
		sql.append(" and b.table_name=d.table_name");
		sql.append(" and c.position=d.position");
		sql.append(" order by c.column_name");

		logger.debug(sql.toString());

		return connection.getpreparedSql(sql.toString());
	}

	public void setDBType(String dbType) {
		this.dbType = dbType;
	}
}