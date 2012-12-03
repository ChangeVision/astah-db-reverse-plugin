package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class DBConnectionTest {

    private ConnectionInfo info;
    private DBConnection connection;

    @Before
    public void before() throws Exception {
        URL jarURL = DBConnectionTest.class.getResource("h2-1.3.170.jar");
        String path = jarURL.getPath();
        info = new ConnectionInfo();
        info.setPathfile(path);
        info.setClassname("org.h2.Driver");
        info.setLogin("sa");
        info.setPassword("");
        URL dataURL = this.getClass().getResource("/data");
        info.setJdbcurl("jdbc:h2:file:" + dataURL.getPath() + "/h2");
        connection = new DBConnection();
    }
    
    @After
    public void after() throws Exception {
        connection.close();
    }
    
    @Test
    @Ignore(value="現状はNullPointerException")
    public void connectWithNull() throws MalformedURLException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        connection.connect(null);
    }
    
    @Test
    public void connectWithH2TestDB() throws Exception {
        connection.connect(info);

        String[] schemas = connection.getSchemas();
        assertThat(schemas.length,is(2));
        assertThat(Arrays.asList(schemas),hasItem("PUBLIC"));
    }
    
    @Test
    public void getSchemas() throws Exception {
        connection.connect(info);
        String[] schemas = connection.getSchemas();
        assertThat(Arrays.asList(schemas),allOf(hasItem("PUBLIC"),hasItem("INFORMATION_SCHEMA")));
    }
    
    @Test
    public void getTables() throws Exception {
        connection.connect(info);
        List<String> tables = connection.getTables(null, "PUBLIC");
        assertThat(tables,hasItem("SAMPLE"));
    }
    
    @Test
    public void getCatalogs() throws Exception {
        connection.connect(info);
        String[] catalogs = connection.getCatalogs();
        assertThat(Arrays.asList(catalogs),hasItem("H2"));
    }
    
    @Test
    public void testname() throws Exception {
        connection.connect(info);
    }

}
