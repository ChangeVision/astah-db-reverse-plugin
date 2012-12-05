package com.change_vision.astah.extension.plugin.dbreverse.reverser.finder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.jude.api.inf.model.IERDomain;
import com.change_vision.jude.api.inf.model.IERSchema;

public class DomainFinderTest {
    
    @Mock
    private IERSchema schema;

    @Mock
    private IERDomain domain;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(schema.getDomains()).thenReturn(new IERDomain[]{
                domain
        });
        when(domain.getName()).thenReturn("address");
        when(domain.getChildren()).thenReturn(new IERDomain[]{});
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void findWithNull() throws Exception {
        DomainFinder finder = new DomainFinder(schema);
        finder.find(null);
    }
    
    @Test
    public void find() throws Exception {
        DomainFinder finder = new DomainFinder(schema);
        IERDomain domain = finder.find("address");
        assertThat(domain,is(notNullValue()));
    }
    
}
