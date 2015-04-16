package com.test.util.zip;

import com.common.util.generic.DateUtil;
import com.common.util.zip.ArchiveUtil;
import java.io.File;
import org.junit.Test;

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
		DateUtil.parseSimpleStringDate("2012-5-22"));
    }
    
    @Test
    public void archive(){
	File file=ArchiveUtil.archive("D:\\archive",new String[]{"d:\\ww.txt"},"zoujianhua2.zip");
	System.out.println(file.exists());
    }
}
