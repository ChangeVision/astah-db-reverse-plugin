package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.AttributeInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ConnectionInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ERRelationshipInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.TableInfo;

public class DBReaderTest {

    private static final String TABLE_NAME_OF_SAMPLE_RELATIONSHIPS = "SAMPLE_RELATIONSHIPS";
    private static final String TABLE_NAME_OF_SAMPLE = "SAMPLE";
    private static final String SCHEMA = "PUBLIC";
    private static final String CATALOG = "H2";
    
    private ConnectionInfo info;
    private DBReader reader;

    @Before
    public void before() throws Exception {
        reader = new DBReader();
        URL jarURL = DBReaderTest.class.getResource("h2-2.1.212.jar");
        String path = jarURL.getPath();
        info = new ConnectionInfo();
        info.setPathfile(path);
        info.setClassname("org.h2.Driver");
        info.setLogin("sa");
        info.setPassword("");
        URL dataURL = this.getClass().getResource("/data");
        info.setJdbcurl("jdbc:h2:file:" + dataURL.getPath() + "/h2");
    }
    
    @After
    public void after() throws Exception {
        reader.close();
    }

    @Test
    @Ignore(value="NullPointerExceptionが投げられる")
    public void connectWithNull() throws MalformedURLException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        reader.connect(null);
    }
    
    @Test
    @Ignore(value="NullPointerExceptionが投げられる")
    public void connectWithNoPathfileConnectionInfo() throws Exception {
        reader.connect(new ConnectionInfo());
    }

    @Test
    public void connectWithTestDB() throws Exception {
        reader.connect(info);
    }
    
    @Test
    public void getTables() throws Exception {
        reader.connect(info);
        List<TableInfo> tables = reader.getTables(CATALOG, SCHEMA);
        assertThat(tables.size(),is(2));
        TableInfo tableInfo = tables.get(0);
        List<AttributeInfo> attributes = tableInfo.getAttributes();
        assertThat(attributes.size(),is(21));
    }
    
    @Test
    public void getSchemas() throws Exception {
        reader.connect(info);
        String[] schemas = reader.getSchemata();
        assertThat(schemas.length,is(2));
        assertThat(Arrays.asList(schemas),allOf(hasItem("PUBLIC"),hasItem("INFORMATION_SCHEMA")));
    }
    
    @Test
    public void getRelations() throws Exception {
        reader.connect(info);
        List<TableInfo> tables = reader.getTables(CATALOG, SCHEMA);
        List<ERRelationshipInfo> relationships = reader.getRelationships(tables);
        assertThat(relationships.size(),is(1));
        for (ERRelationshipInfo relationshipInfo : relationships) {
            System.out.println(relationshipInfo);
        }
    }
    
    @Test
    public void getFKs() throws Exception {
        reader.connect(info);
        List<TableInfo> tables = reader.getTables(CATALOG, SCHEMA);
        for (TableInfo tableInfo : tables) {
            if (tableInfo.getName().equals(TABLE_NAME_OF_SAMPLE_RELATIONSHIPS)) {
                HashMap<String, String> foreignKeys = reader.getFKs(tableInfo);
                assertThat(foreignKeys.size(),is(1));
                System.out.println(foreignKeys);
            }
        }
    }
    
    @Test
    public void getPKs() throws Exception {
        reader.connect(info);
        List<TableInfo> tables = reader.getTables(CATALOG, SCHEMA);
        for (TableInfo tableInfo : tables) {
            if (tableInfo.getName().equals(TABLE_NAME_OF_SAMPLE)) {
                HashSet<String> primaryKeys = reader.getPKs(tableInfo);
                assertThat(primaryKeys.size(),is(1));
                assertThat(primaryKeys,hasItem("TEST_IDENTITY"));
            }
        }
    }

}
