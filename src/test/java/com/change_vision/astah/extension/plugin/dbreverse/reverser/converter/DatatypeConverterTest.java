package com.change_vision.astah.extension.plugin.dbreverse.reverser.converter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.astah.extension.plugin.dbreverse.reverser.model.DatatypeInfo;
import com.change_vision.jude.api.inf.editor.ERModelEditor;
import com.change_vision.jude.api.inf.model.IERDatatype;
import com.change_vision.jude.api.inf.model.IERModel;

public class DatatypeConverterTest {
    
    @Mock
    private ERModelEditor editor;

    @Mock
    private IERModel erModel;

    @Mock
    private IERDatatype datatype;

    private DatatypeConverter converter;
    
    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void convertWithNull() throws Exception {
        converter = new DatatypeConverter(editor,erModel);
        DatatypeInfo datatypeInfo = null;
        converter.convert(datatypeInfo);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void convertWithEmptyName() throws Exception {
        converter = new DatatypeConverter(editor,erModel);
        DatatypeInfo datatypeInfo = new DatatypeInfo();
        converter.convert(datatypeInfo);
    }
    
    @Test
    public void convert() throws Exception {
        when(editor.createERDatatype(erModel, "hoge")).thenReturn(datatype);
        converter = new DatatypeConverter(editor,erModel);
        DatatypeInfo datatypeInfo = new DatatypeInfo();
        datatypeInfo.setName("hoge");
        IERDatatype converted = converter.convert(datatypeInfo);
        assertThat(converted,is(notNullValue()));
    }
    
    @Test
    public void convertDetail() throws Exception {
        when(editor.createERDatatype(erModel, "hoge")).thenReturn(datatype);
        converter = new DatatypeConverter(editor,erModel);
        DatatypeInfo datatypeInfo = new DatatypeInfo();
        datatypeInfo.setName("hoge");
        datatypeInfo.setLengthConstraint("10");
        datatypeInfo.setPrecisionConstraint("12");
        converter.convert(datatypeInfo);
        verify(datatype).setLengthConstraint("10");
        verify(datatype).setPrecisionConstraint("12");
    }

}
