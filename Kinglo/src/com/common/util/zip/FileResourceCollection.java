package com.common.util.zip;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
/**
 * 
 * 类的描述:文件资源集合类
 * 创建人:邹建华
 * 创建时间:2015-4-11
 */
public final class FileResourceCollection implements ResourceCollection {
    List<FileResource> list = new ArrayList<FileResource>();
    
    /**
     * 添加文件到集合中
     * @param file	File对象
     */
    public void add(File file) {
	if ((file == null) || (!file.exists())) {
	    return;
	}
	this.list.add(new FileResource(file));
    }

    public List<FileResource> getFileResourceList() {
	return this.list;
    }

    public boolean isFilesystemOnly() {
	return true;
    }

    public Iterator<?> iterator() {
	return this.list.iterator();
    }

    public int size() {
	return this.list.size();
    }
}
