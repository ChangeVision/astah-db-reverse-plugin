package com.change_vision.astah.extension.plugin.dbreverse.reverser.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.converter.TableConverter;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.TableInfo;
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.model.IERSchema;

public class TableConverterTest {
    
    @Mock
    private ERModelEditor editor;
    
    @Mock
    private IERSchema schema;

    @Mock
    private IEREntity entity;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void convertEntity() throws Exception {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setName("hoge");
        
        when(editor.createEREntity(schema, "hoge","hoge")).thenReturn(entity);
        TableConverter converter = new TableConverter(editor,schema);
        IEREntity converted = converter.convert(tableInfo);
        assertThat(converted,is(notNullValue()));
        verify(converted).setDefinition(anyString());
        verify(converted).setDefinition(anyString());
    }

}
