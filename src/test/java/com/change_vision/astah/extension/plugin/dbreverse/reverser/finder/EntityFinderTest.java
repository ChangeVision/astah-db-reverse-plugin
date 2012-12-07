package com.change_vision.astah.extension.plugin.dbreverse.reverser.finder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.change_vision.jude.api.inf.model.IEREntity;
import com.change_vision.jude.api.inf.model.IERSchema;

public class EntityFinderTest {
    
    @Mock
    private IERSchema schema;
    
    @Mock
    private IEREntity entity;

    private EntityFinder finder;
    
    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(schema.getEntities()).thenReturn(new IEREntity[]{
                entity
        });
        when(entity.getName()).thenReturn("entity0");
        finder = new EntityFinder();
    }
    
    @Test
    public void find() throws Exception {
        IEREntity entity = finder.find(schema, "entity0");
        assertThat(entity,is(notNullValue()));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void findWithNullName() throws Exception {
        finder.find(schema, null);
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void findWithNullSchema() throws Exception {
        finder.find(null,"entity0");
    }

}
