package com.change_vision.astah.extension.plugin.dbreverse.reverser.finder;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.jude.api.inf.model.IERDatatype;
import com.change_vision.jude.api.inf.model.IERSchema;

public class DatatypeFinderTest {
    
    @Mock
    private IERSchema schema;
    
    @Mock
    private IERDatatype datatype;

    private DatatypeFinder finder;
    
    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(schema.getDatatypes()).thenReturn(new IERDatatype[]{
                datatype
        });
        when(datatype.getName()).thenReturn("hoge");
        finder = new DatatypeFinder(schema);
    }

    @Test(expected=IllegalArgumentException.class)
    public void findWithNull() throws Exception {
        finder.find(null);
    }
    
    @Test
    public void find() throws Exception {
        IERDatatype found = finder.find("hoge");
        assertThat(found,is(notNullValue()));
    }
    
    @Test
    public void notFound() throws Exception {
        IERDatatype found = finder.find("fuga");
        assertThat(found,is(nullValue()));
    }

}
