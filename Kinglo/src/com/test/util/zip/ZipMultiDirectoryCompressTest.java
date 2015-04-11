package com.test.util.zip;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.coscon.shipsuite.common.util.zip.ZipMultiDirectoryCompress;

public class ZipMultiDirectoryCompressTest {

    @Test
    public void testZipStringString() {
	ZipMultiDirectoryCompress.zip("D:\\archive","D:\\zoujianhua.zip");
    }

    @Test
    public void testZipStringStringCollectionOfString() {
	List<String> list=new ArrayList<String>();
	list.add("aaa.txt");
	ZipMultiDirectoryCompress.zip("D:\\archive", "D:\\zoujianhua3.zip", list);
    }
}
