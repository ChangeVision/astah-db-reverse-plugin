package com.change_vision.astah.extension.plugin.dbreverse.reverser.model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AttributeInfoTest {

    private AttributeInfo info;

    @Before
    public void before() throws Exception {
        info = new AttributeInfo();
    }
    
    @Test
    public void getLengthPrecisionOnlyLength() {
        info.setLength("10");
        assertThat(info.getLengthPrecision(),is("10"));
    }
    
    @Test
    public void getLengthPrecisionOnlyPrecision() throws Exception {
        info.setPrecision("10");
        assertThat(info.getLengthPrecision(),is("10"));
    }
    
    @Test
    public void getLengthPrecision() throws Exception {
        info.setLength("10");
        info.setPrecision("10");
        assertThat(info.getLengthPrecision(),is("10,10"));
    }

}
