package com.test.util.string;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.coscon.shipsuite.common.util.string.IdGenerator;

public class IdGeneratorTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getUUID() {
	for (int i = 0; i < 10; i++) {
	    Assert.assertEquals(32, IdGenerator.getUUID().length());
	    System.out.println(IdGenerator.getUUID());
	}
    }

    @Test
    public void getTimestampSeq() {
	for (int i = 0; i < 11000; i++) {
	    System.out.println(IdGenerator.getTimestampSeq());
	}
    }

    @Test
    public void getTimestamp() {
	for (int i = 0; i < 11000; i++) {
	    System.out.println(IdGenerator.getTimestamp(Integer.valueOf(6)));
	}
    }
}
