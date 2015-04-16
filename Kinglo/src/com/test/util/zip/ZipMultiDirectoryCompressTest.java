package com.test.util.zip;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.common.util.zip.ZipMultiDirectoryCompress;

public class ZipMultiDirectoryCompressTest {

    @Test
    public void testZipStringString() {
	ZipMultiDirectoryCompress.zip("D:\\archive","D:\\zoujianhua.zip");
    }

    @Test
    public void testZipStringStringCollectionOfString() {
	List<String> list=new ArrayList<String>();
	list.add("D:\\archive\\aaa.txt");
	ZipMultiDirectoryCompress.zip("D:\\archive", "D:\\zoujianhua3.zip", list);
    }
}
