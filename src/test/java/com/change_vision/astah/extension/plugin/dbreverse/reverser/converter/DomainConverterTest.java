package com.change_vision.astah.extension.plugin.dbreverse.reverser.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.finder.DatatypeFinder;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DatatypeInfo;
import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DomainInfo;
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.model.IERDatatype;
import com.change_vision.jude.api.inf.model.IERDomain;
import com.change_vision.jude.api.inf.model.IERModel;
import com.change_vision.jude.api.inf.model.IERSchema;

public class DomainConverterTest {
    
    @Mock
    private IERModel erModel = null;

    @Mock
    private ERModelEditor editor = null;
    
    @Mock
    private DatatypeFinder datatypeFinder = null;

    @Mock
    private IERDatatype datatype;

    @Mock
    private IERDomain erDomain;

    @Mock
    private DatatypeConverter datatypeConverter;

    private DomainConverter converter;
    
    private DomainInfo domainInfo;

    private IERSchema schema;


    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        domainInfo = createDomainInfo();
        when(datatype.getName()).thenReturn("int");
        when(editor.createERDomain(erModel, null, "address", "address", datatype)).thenReturn(erDomain);
        when(erModel.getSchemata()).thenReturn(new IERSchema[]{
                schema
        });
        DatatypeInfo datatypeInfo = new DatatypeInfo();
        datatypeInfo.setName("int");
        when(datatypeConverter.convert(datatypeInfo)).thenReturn(datatype);
        converter = new DomainConverter(editor,erModel);
    }

    private DomainInfo createDomainInfo() {
        DomainInfo domainInfo = new DomainInfo();
        domainInfo.setName("address");
        domainInfo.setDatatypeName("int");
        return domainInfo;
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void convertWithNull() throws Exception {
        converter.convert(null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void convertWithEmptyName() throws Exception {
        DomainInfo emptyNameDomainInfo = new DomainInfo();
        converter.convert(emptyNameDomainInfo);
    }
    
    @Test
    public void convertWithExistedDatatype() throws Exception {
        converter.setDatatypeFinder(datatypeFinder);
        when(datatypeFinder.find("int")).thenReturn(datatype);
        
        IERDomain converted = converter.convert(domainInfo);
        assertThat(converted,is(notNullValue()));
    }
    
    @Test
    public void convertWithNotExistedDatatype() throws Exception {
        converter.setDatatypeFinder(datatypeFinder);
        converter.setDatatypeConverter(datatypeConverter);
        
        IERDomain converted = converter.convert(domainInfo);
        assertThat(converted,is(notNullValue()));
    }

}
