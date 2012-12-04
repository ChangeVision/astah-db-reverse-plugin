package com.change_vision.astah.extension.plugin.dbreverse.reverser.converter;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.AttributeInfo;
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.model.IERAttribute;
import com.change_vision.jude.api.inf.model.IERDatatype;
import com.change_vision.jude.api.inf.model.IERDomain;
import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.model.IERModel;
import com.change_vision.jude.api.inf.model.IERSchema;

public class AttributeConverterTest {
    
    private static final String CREATE_ATTRIBUTE_NAME = "hoge";

    private AttributeConverter converter;

    @Mock
    private ERModelEditor editor;
    
    @Mock
    private IEREntity entity;
    
    @Mock
    private IERModel erModel;
    
    @Mock
    private IERSchema schema;

    @Mock
    private IERDatatype dataType;
    
    @Mock
    private IERAttribute attribute;
    
    @Mock
    private IERDomain domain;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        converter = new AttributeConverter(editor, erModel);
        when(erModel.getSchemata()).thenReturn(new IERSchema[]{
           schema 
        });
        when(schema.getDatatypes()).thenReturn(new IERDatatype[]{
                dataType
        });
        when(dataType.getName()).thenReturn("int");
        when(schema.getDomains()).thenReturn(new IERDomain[]{
                domain
        });
        when(domain.getName()).thenReturn("address");
        when(domain.getChildren()).thenReturn(new IERDomain[]{});
    }

    @Test(expected=IllegalArgumentException.class)
    public void convertWithNullEntity() throws Exception {
        IEREntity entity = null;
        AttributeInfo attributeInfo = new AttributeInfo();
        
        IERAttribute attribute = converter.convert(entity,attributeInfo);
        assertThat(attribute,is(nullValue()));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void convertWithNullAttribute() throws Exception {
        AttributeInfo attributeInfo = null;
        
        converter.convert(entity,attributeInfo);
    }
    
    @Test
    public void convertAttributeByDataType() throws Exception {
        when(editor.createERAttribute(entity, CREATE_ATTRIBUTE_NAME , CREATE_ATTRIBUTE_NAME, dataType)).thenReturn(attribute);
        AttributeInfo attributeInfo = createAttributeInfo();
        
        IERAttribute attribute = converter.convert(entity,attributeInfo);
        assertThat(attribute,is(notNullValue()));
        verify(editor).createERAttribute(entity, CREATE_ATTRIBUTE_NAME , CREATE_ATTRIBUTE_NAME, dataType);
    }
    
    @Test
    public void convertAttributeByDomain() throws Exception {
        when(editor.createERAttribute(entity, CREATE_ATTRIBUTE_NAME , CREATE_ATTRIBUTE_NAME, domain)).thenReturn(attribute);
        
        AttributeInfo attributeInfo = new AttributeInfo();
        attributeInfo.setName(CREATE_ATTRIBUTE_NAME);
        attributeInfo.getDomain().setName("address");
        
        IERAttribute attribute = converter.convert(entity,attributeInfo);
        assertThat(attribute,is(notNullValue()));
        verify(editor).createERAttribute(entity, CREATE_ATTRIBUTE_NAME , CREATE_ATTRIBUTE_NAME, domain);
    }
    
    @Test
    public void setPKAfterConverted() throws Exception {
        when(editor.createERAttribute(entity, CREATE_ATTRIBUTE_NAME , CREATE_ATTRIBUTE_NAME, dataType)).thenReturn(attribute);
        
        AttributeInfo attributeInfo = createAttributeInfo();
        attributeInfo.setPK(true);
        
        IERAttribute attribute = converter.convert(entity,attributeInfo);
        verify(attribute).setPrimaryKey(true);
        verify(attribute).setNotNull(true);
    }
    
    @Test
    public void setNotNullAfterConverted() throws Exception {
        when(editor.createERAttribute(entity, CREATE_ATTRIBUTE_NAME , CREATE_ATTRIBUTE_NAME, dataType)).thenReturn(attribute);
        
        AttributeInfo attributeInfo = createAttributeInfo();
        attributeInfo.setNotNull(true);
        
        IERAttribute attribute = converter.convert(entity,attributeInfo);
        verify(attribute).setNotNull(true);
    }

    @Test
    public void setDefaultValueAfterConverted() throws Exception {
        when(editor.createERAttribute(entity, CREATE_ATTRIBUTE_NAME , CREATE_ATTRIBUTE_NAME, dataType)).thenReturn(attribute);
        
        AttributeInfo attributeInfo = createAttributeInfo();
        attributeInfo.setDefaultValue("default");
        
        IERAttribute attribute = converter.convert(entity,attributeInfo);
        verify(attribute).setDefaultValue("default");
    }
    
    @Test
    public void setDefinitionAfterConverted() throws Exception {
        when(editor.createERAttribute(entity, CREATE_ATTRIBUTE_NAME , CREATE_ATTRIBUTE_NAME, dataType)).thenReturn(attribute);
        
        AttributeInfo attributeInfo = createAttributeInfo();
        attributeInfo.setDefinition("definition");
        
        IERAttribute attribute = converter.convert(entity,attributeInfo);
        verify(attribute).setDefinition("definition");
    }
    
    @Test
    public void setLengthPrecisionAfterConverted() throws Exception {
        when(editor.createERAttribute(entity, CREATE_ATTRIBUTE_NAME , CREATE_ATTRIBUTE_NAME, dataType)).thenReturn(attribute);
        
        AttributeInfo attributeInfo = createAttributeInfo();
        attributeInfo.setLength("10");
        attributeInfo.setPrecision("10");
        
        IERAttribute attribute = converter.convert(entity,attributeInfo);
        verify(attribute).setLengthPrecision("10,10");
    }

    private AttributeInfo createAttributeInfo() {
        AttributeInfo attributeInfo = new AttributeInfo();
        attributeInfo.setName(CREATE_ATTRIBUTE_NAME);
        attributeInfo.getDataType().setName("int");
        return attributeInfo;
    }
    
}
