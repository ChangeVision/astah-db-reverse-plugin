package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ConnectionInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.ERRelationshipInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.TableInfo;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.model.IERModel;
import com.change_vision.jude.api.inf.model.IERSchema;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

public class DBToProjectTest {
	
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
    private DBReader reader;
    private List<TableInfo> tables;
    private List<ERRelationshipInfo> relationships;
	
	@Before
    public void before() throws Exception {
        reader = new DBReader();
        URL jarURL = DBToProjectTest.class.getResource("h2-1.3.170.jar");
        String path = jarURL.getPath();
        ConnectionInfo info = new ConnectionInfo();
        info.setPathfile(path);
        info.setClassname("org.h2.Driver");
        info.setLogin("sa");
        info.setPassword("");
        URL dataURL = this.getClass().getResource("/data");
        info.setJdbcurl("jdbc:h2:file:" + dataURL.getPath() + "/h2");
        reader.connect(info);
        tables = reader.getTables("H2", "PUBLIC");
        relationships = reader.getRelationships("H2", "PUBLIC");
    }
	
	@After
    public void after() throws Exception {
	    if (TransactionManager.isInTransaction()) {
            TransactionManager.abortTransaction();
        }
        reader.close();
    }

	@Test
	public void importToProjectWithSpecifyNotExistFile() throws Exception {
		DBToProject dbToProject = new DBToProject();
		File projectFile = folder.newFile("test.asta");
		String projectFilePath = projectFile.getAbsolutePath();
		
		dbToProject.importToProject(projectFilePath, tables, relationships);
		ProjectAccessor accessor = ProjectAccessorFactory.getProjectAccessor();
		accessor.open(projectFile.getAbsolutePath());
		
		INamedElement[] elements = accessor.findElements(IERModel.class);
		assertThat(elements,is(notNullValue()));
		
		IERModel erModel = (IERModel)elements[0];
		INamedElement[] ownedElements = erModel.getOwnedElements();
		assertThat(ownedElements.length,is(1));
		INamedElement candidatesOfSchema = ownedElements[0];
        assertThat(candidatesOfSchema,is(instanceOf(IERSchema.class)));
        IERSchema schema = (IERSchema) candidatesOfSchema;
        IEREntity[] entities = schema.getEntities();
        assertThat(entities.length,is(2));
        assertThat(entities[0].getName(),is("SAMPLE"));
        assertThat(entities[1].getName(),is("SAMPLE_RELATIONSHIPS"));
	}
	
	@Test
	public void importProjectWithSpecifyEmptyFile() throws Exception {
		DBToProject dbToProject = new DBToProject();		
		URL resource = getClass().getResource("empty.asta");

		String projectFilePath = resource.getFile();
		dbToProject.importToProject(projectFilePath , tables, relationships);
	}
	

}