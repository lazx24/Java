package com.test.util.zip;

import java.io.File;

import org.junit.Test;

import com.common.util.date.DateUtil;
import com.common.util.zip.ArchiveUtil;

public class ArchiveUtilTest {
    @Test
    public void archvie() {
	File file = ArchiveUtil.archive("D:\\archive", new File[] {
		new File("d:\\a.txt"), new File("d:\\b.txt") });
	System.out.println(file.exists());
    }

    @Test
    public void archvieEndDate() {
	ArchiveUtil.archive("D:\\archive", "d:\\aaa",
		DateUtil.stringFormatToDate("2012-5-22",DateUtil.DAY_DATE_FORMAT));
    }
    
    @Test
    public void archive(){
	File file=ArchiveUtil.archive("D:\\archive",new String[]{"d:\\ww.txt"},"zoujianhua2.zip");
	System.out.println(file.exists());
    }
}
