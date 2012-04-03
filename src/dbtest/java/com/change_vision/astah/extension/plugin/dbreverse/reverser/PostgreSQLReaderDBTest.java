package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgreSQLReaderDBTest extends ReverserUtil {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@AfterClass
	public static void tearDown() throws SQLException {
		getDBReader().close();
	}

	private static final Logger logger = LoggerFactory.getLogger(PostgreSQLReaderDBTest.class);

	public PostgreSQLReaderDBTest() throws MalformedURLException, SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		super("postgresql.properties");
	}

	@Test
	public final void testDBReader() {
	}

	@Test
	public final void testGetInstance() {
	}

	@Test
	public final void testConnect() {
	}

	@Test
	public final void testClose() {
	}

	@Test
	public final void testGetSchemas() {
	}

	@Test
	public final void testGetPKs() throws SQLException {
		HashSet<String> pks = getDBReader().getPKs(null, getSchemaName(), "ad_wf_process");
		for (String pk : pks) {
			logger.debug("Primary Key = " + pk);
		}
	}

	@Test
	public final void testGetFKs() throws SQLException {
		Map<String, String> fks = getDBReader().getFKs(null, getSchemaName(), "ad_wf_process");
		for (String fk : fks.keySet()) {
			logger.debug("Foreign Key = " + fk);
		}
	}

	@Test
	public final void testGetTables() throws SQLException {
		List<TableInfo> tables = getDBReader().getTables(null, getSchemaName());
		for (TableInfo table : tables) {
			if (table.getName().equals("ad_wf_process")) {
				logger.debug("Name : " + table.getName());
				logger.debug("Definition : " + table.getDefinition());
			}
		}
	}

	@Test
	public final void testGetRelationships() throws SQLException {
		List<ERRelationshipInfo> relationships =  getDBReader().getRelationships(null, getSchemaName());
		for (ERRelationshipInfo relationship : relationships) {
			if (relationship.getParentTable().equals(relationship.childTable)) {
				logger.info("----- リレーションが親と子で同一 ----");
				logger.info("            Name : " + relationship.getName());
				logger.info("     ParentTable : " + relationship.getParentTable());
				logger.info("      ChildTable : " + relationship.childTable);
				logger.info("   isIdentifying : " + relationship.isIdentifying());
				logger.info("  isMultiToMulti : " + relationship.isMultiToMulti());
				logger.info("isNonIdentifying : " + relationship.isNonIdentifying());
				logger.info("isParentRequired : " + relationship.isParentRequired());
//			} else {
//				logger.info("-----");
//				logger.info("       Name : " + relationship.getName());
//				logger.info("ParentTable : " + relationship.getParentTable());
//				logger.info(" ChildTable : " + relationship.childTable);
			}
		}
	}
}