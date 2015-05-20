package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ConnectionInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ERRelationshipInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.TableInfo;
import com.change_vision.astah.extension.plugin.dbreverse.util.DatabaseTypes;

public class ImportToProjectTest {
    
    @Mock
    private DBReader reader;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void importFromRealH2Repository() throws Exception {
        reader = new DBReader();
        URL jarURL = ImportToProjectTest.class.getResource("h2-1.3.170.jar");
        String path = jarURL.getPath();
        ConnectionInfo info = new ConnectionInfo();
        info.setPathfile(path);
        info.setClassname("org.h2.Driver");
        info.setLogin("sa");
        info.setPassword("");
        URL dataURL = this.getClass().getResource("/data");
        info.setJdbcurl("jdbc:h2:file:" + dataURL.getPath() + "/h2");
        reader.connect(info);
        ImportToProject importer = new ImportToProject(reader);
        boolean imported;
        try {
            imported = importer.doImport(DatabaseTypes.H2.getType(),"PUBLIC");
        } finally{
            reader.close();
        }
        assertThat(imported,is(true));
    }
    
    @Test
    public void importFromMySQL() throws Exception {
        List<TableInfo> dummyInfo = new ArrayList<TableInfo>();
        when(reader.getTables("PUBLIC", null)).thenReturn(dummyInfo);
        when(reader.getRelationships(dummyInfo)).thenReturn(new ArrayList<ERRelationshipInfo>());
        
        ImportToProject importer = new ImportToProject(reader);
        
        boolean imported = importer.doImport(DatabaseTypes.MYSQL.getType(), "PUBLIC");
        assertThat(imported,is(true));
    }
    
    @Test
    public void importFromPostgreSQL() throws Exception {
        List<TableInfo> dummyInfo = new ArrayList<TableInfo>();
        when(reader.getTables(null, "PUBLIC")).thenReturn(dummyInfo);
        when(reader.getRelationships(dummyInfo)).thenReturn(new ArrayList<ERRelationshipInfo>());
        
        ImportToProject importer = new ImportToProject(reader);
        boolean imported = importer.doImport(DatabaseTypes.POSTGRES.getType(), "PUBLIC");
        assertThat(imported,is(true));
    }

}
