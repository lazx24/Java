package com.test.util.zip;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.coscon.shipsuite.common.util.file.ServiceFile;
import com.coscon.shipsuite.common.util.zip.Zipper;

public class ZipperTest {

    @Test
    public void testUnZip() throws IOException {
	Zipper.unZip("D:\\zoujianhua.zip", "D:\\aaa", new String[]{"ww.txt"});
    }

    @Test
    public void testUnZip2ServiceFileStringStringArray() throws IOException {
	List<ServiceFile> list=Zipper.unZip2ServiceFile("D:\\zoujianhua.zip", new String[]{"a.txt"});
	
	for (int i = 0; i < list.size(); i++) {
	    ServiceFile files=list.get(i);
	    System.out.println(files.getFilename()+";"+files.getLocalFilename()+";"+new String(files.getData()));
	}
    }

    @Test
    public void testZipCollectionOfFileStringDateDateBoolean() {
	List<File> list=new ArrayList<File>();
	list.add(new File("D:\\a.txt"));
	list.add(new File("D:\\ww.txt"));
	Zipper.zip(list, "D:\\zoujianhua3.zip", null, null, true);
    }
}
