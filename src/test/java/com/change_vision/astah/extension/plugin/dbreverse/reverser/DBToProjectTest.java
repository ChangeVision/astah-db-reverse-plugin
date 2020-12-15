package com.change_vision.astah.extension.plugin.dbreverse.reverser;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

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
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.editor.ModelEditorFactory;
import com.change_vision.jude.api.inf.editor.TransactionManager;
import com.change_vision.jude.api.inf.model.IERAttribute;
import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.model.IERIndex;
import com.change_vision.jude.api.inf.model.IERModel;
import com.change_vision.jude.api.inf.model.IERRelationship;
import com.change_vision.jude.api.inf.model.IERSchema;
import com.change_vision.jude.api.inf.model.IModel;
import com.change_vision.jude.api.inf.model.INamedElement;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.project.ProjectAccessorFactory;

public class DBToProjectTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private DBReader reader;
    private List<TableInfo> tables;
    private List<ERRelationshipInfo> relationships;
    private IModel project;
    private ProjectAccessor accessor;

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
        relationships = reader.getRelationships(tables);
        File projectFile = folder.newFile("test.asta");
        String projectFilePath = projectFile.getAbsolutePath();
        accessor = ProjectAccessorFactory.getProjectAccessor();
        accessor.create(projectFilePath);
        project = accessor.getProject();
    }

    @After
    public void after() throws Exception {
        if (TransactionManager.isInTransaction()) {
            TransactionManager.abortTransaction();
        }
        reader.close();
    }

    @Test
    public void importToProjectDetail() throws Exception {
        ERModelEditor editor = ModelEditorFactory.getERModelEditor();
        DBToProject dbToProject = new DBToProject(editor);
        dbToProject.importToProject(project, tables, relationships);

        INamedElement[] elements = accessor.findElements(IERModel.class);
        assertThat(elements, is(notNullValue()));

        IERModel erModel = (IERModel) elements[0];
        INamedElement[] ownedElements = erModel.getOwnedElements();
        assertThat(ownedElements.length, is(1));

        INamedElement candidatesOfSchema = ownedElements[0];
        assertThat(candidatesOfSchema, is(instanceOf(IERSchema.class)));
        IERSchema schema = (IERSchema) candidatesOfSchema;
        IEREntity[] entities = schema.getEntities();
        assertThat(entities.length, is(2));

        IEREntity sampleEntity = entities[0];
        assertThat(sampleEntity.getName(), is("SAMPLE"));

        IERAttribute[] sampleEntityPrimaryKeys = sampleEntity.getPrimaryKeys();
        assertThat(sampleEntityPrimaryKeys.length, is(1));
        assertThat(sampleEntityPrimaryKeys[0].getName(), is("TEST_IDENTITY"));

        IERAttribute[] sampleEntityNonPrimaryKeys = sampleEntity.getNonPrimaryKeys();
        assertThat(sampleEntityNonPrimaryKeys.length, is(20));
        assertThat(sampleEntityNonPrimaryKeys[0].getName(), is("TEST_INT"));
        assertThat(sampleEntityNonPrimaryKeys[1].getName(), is("TEST_BOOLEAN"));
        assertThat(sampleEntityNonPrimaryKeys[2].getName(), is("TEST_TINYINT"));
        assertThat(sampleEntityNonPrimaryKeys[3].getName(), is("TEST_SMALLINT"));
        assertThat(sampleEntityNonPrimaryKeys[4].getName(), is("TEST_BIGINT"));
        assertThat(sampleEntityNonPrimaryKeys[5].getName(), is("TEST_DECIMAL"));
        assertThat(sampleEntityNonPrimaryKeys[6].getName(), is("TEST_DOUBLE"));
        assertThat(sampleEntityNonPrimaryKeys[7].getName(), is("TEST_REAL"));
        assertThat(sampleEntityNonPrimaryKeys[8].getName(), is("TEST_TIME"));
        assertThat(sampleEntityNonPrimaryKeys[9].getName(), is("TEST_DATE"));
        assertThat(sampleEntityNonPrimaryKeys[10].getName(), is("TEST_TIMESTAMP"));
        assertThat(sampleEntityNonPrimaryKeys[11].getName(), is("TEST_BINARY"));
        assertThat(sampleEntityNonPrimaryKeys[12].getName(), is("TEST_OTHER"));
        assertThat(sampleEntityNonPrimaryKeys[13].getName(), is("TEST_VARCHAR"));
        assertThat(sampleEntityNonPrimaryKeys[14].getName(), is("TEST_VARCHAR_IGNORECASE"));
        assertThat(sampleEntityNonPrimaryKeys[15].getName(), is("TEST_CHAR"));
        assertThat(sampleEntityNonPrimaryKeys[16].getName(), is("TEST_BLOB"));
        assertThat(sampleEntityNonPrimaryKeys[17].getName(), is("TEST_CLOB"));
        assertThat(sampleEntityNonPrimaryKeys[18].getName(), is("TEST_UUID"));
        assertThat(sampleEntityNonPrimaryKeys[19].getName(), is("TEST_ARRAY"));

        assertThat(sampleEntityNonPrimaryKeys[0].getDatatype().getName(), is("INTEGER"));
        assertThat(sampleEntityNonPrimaryKeys[1].getDatatype().getName(), is("BOOLEAN"));
        assertThat(sampleEntityNonPrimaryKeys[2].getDatatype().getName(), is("TINYINT"));
        assertThat(sampleEntityNonPrimaryKeys[3].getDatatype().getName(), is("SMALLINT"));
        assertThat(sampleEntityNonPrimaryKeys[4].getDatatype().getName(), is("BIGINT"));
        assertThat(sampleEntityNonPrimaryKeys[5].getDatatype().getName(), is("DECIMAL"));
        assertThat(sampleEntityNonPrimaryKeys[6].getDatatype().getName(), is("DOUBLE"));
        assertThat(sampleEntityNonPrimaryKeys[7].getDatatype().getName(), is("REAL"));
        assertThat(sampleEntityNonPrimaryKeys[8].getDatatype().getName(), is("TIME"));
        assertThat(sampleEntityNonPrimaryKeys[9].getDatatype().getName(), is("DATE"));
        assertThat(sampleEntityNonPrimaryKeys[10].getDatatype().getName(), is("TIMESTAMP"));
        assertThat(sampleEntityNonPrimaryKeys[11].getDatatype().getName(), is("VARBINARY"));
        assertThat(sampleEntityNonPrimaryKeys[12].getDatatype().getName(), is("OTHER"));
        assertThat(sampleEntityNonPrimaryKeys[13].getDatatype().getName(), is("VARCHAR"));
        assertThat(sampleEntityNonPrimaryKeys[14].getDatatype().getName(), is("VARCHAR_IGNORECASE"));
        assertThat(sampleEntityNonPrimaryKeys[15].getDatatype().getName(), is("CHAR"));
        assertThat(sampleEntityNonPrimaryKeys[16].getDatatype().getName(), is("BLOB"));
        assertThat(sampleEntityNonPrimaryKeys[17].getDatatype().getName(), is("CLOB"));
        assertThat(sampleEntityNonPrimaryKeys[18].getDatatype().getName(), is("UUID"));
        assertThat(sampleEntityNonPrimaryKeys[19].getDatatype().getName(), is("ARRAY"));

        IERIndex[] indices = sampleEntity.getERIndices();
        assertThat(indices.length, is(2));

        IERIndex index0 = indices[0];
        assertThat(index0.getName(), is("CONSTRAINT_INDEX_9"));

        IERAttribute[] attributes0 = index0.getERAttributes();
        assertThat(attributes0.length, is(1));

        IERAttribute index0Attribute = attributes0[0];
        assertThat(index0Attribute.getName(), is("TEST_INT"));
        assertThat(index0Attribute.getDatatype().getName(), is("INTEGER"));

        IERIndex index1 = indices[1];
        assertThat(index1.getName(), is("PRIMARY_KEY_9"));

        IERAttribute[] attributes1 = index1.getERAttributes();
        assertThat(attributes1.length, is(1));

        IERAttribute index1Attribute = attributes1[0];
        assertThat(index1Attribute.getName(), is("TEST_IDENTITY"));
        assertThat(index1Attribute.isPrimaryKey(), is(true));

        IERRelationship[] parentRelationships = sampleEntity.getParentRelationships();
        assertThat(parentRelationships.length, is(0));

        IERRelationship[] childrenRelationships = sampleEntity.getChildrenRelationships();
        assertThat(childrenRelationships.length, is(1));

        IEREntity sampleRelationsEntity = entities[1];
        assertThat(sampleRelationsEntity.getName(), is("SAMPLE_RELATIONSHIPS"));
        assertThat(childrenRelationships[0].getChild(), is(sampleRelationsEntity));

    }

}