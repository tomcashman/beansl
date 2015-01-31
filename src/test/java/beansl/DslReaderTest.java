/**
 * Copyright 2015 Thomas Cashman
 */
package beansl;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

import beansl.util.TestDslParent;

/**
 *
 * @author Thomas Cashman
 */
public class DslReaderTest {

    @Test
    public void testParseDsl() throws Exception {
        DslReader<TestDslParent> reader = new DslReader<TestDslParent>(TestDslParent.class);
        TestDslParent result = reader.read(new File(getClass().getClassLoader().getResource("test.dsl").getFile()));
        Assert.assertEquals("hello", result.getStr());
        Assert.assertEquals(23, result.getValue());
        Assert.assertEquals(2, result.getItems().size());
        Assert.assertEquals('0', result.getItems().get(0));
        Assert.assertEquals('1', result.getItems().get(1));
        Assert.assertEquals(true, result.getChild() != null);
        Assert.assertEquals(24L, result.getChild().getValue());
    }
}
