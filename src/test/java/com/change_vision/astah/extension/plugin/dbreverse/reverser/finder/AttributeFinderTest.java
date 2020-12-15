package com.change_vision.astah.extension.plugin.dbreverse.reverser.finder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.jude.api.inf.model.IERAttribute;
import com.change_vision.jude.api.inf.model.IEREntity;

public class AttributeFinderTest {
    
    @Mock
    private IEREntity entity;
    
    @Mock
    private IERAttribute pkAttribute;
    
    @Mock
    private IERAttribute nonPkAttribute;

    private AttributeFinder finder;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(entity.getPrimaryKeys()).thenReturn(new IERAttribute[]{
                pkAttribute
        });
        when(pkAttribute.getName()).thenReturn("PK");
        when(entity.getNonPrimaryKeys()).thenReturn(new IERAttribute[]{
                nonPkAttribute
        });
        when(nonPkAttribute.getName()).thenReturn("nonPK");
        finder = new AttributeFinder();
    }

    @Test
    public void notfound() throws Exception {
        IERAttribute attribute = finder.find(entity,"hoge");
        assertThat(attribute,is(nullValue()));
    }
    
    @Test
    public void findPK() throws Exception {
        IERAttribute attribute = finder.find(entity,"PK");
        assertThat(attribute,is(notNullValue()));
    }
    
    @Test
    public void findNonPK() throws Exception {
        IERAttribute attribute = finder.find(entity,"nonPK");
        assertThat(attribute,is(notNullValue()));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void findWithNullName() throws Exception {
        finder.find(entity,null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void findWithNullEntity() throws Exception {
        finder.find(null,"PK");
    }

}
