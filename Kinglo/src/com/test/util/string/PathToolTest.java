package com.test.util.string;

import org.junit.Test;

import com.common.util.string.PathTool;

public class PathToolTest {

	@Test
	public void testGetWebRootPath() {
		System.out.println("根据class获取web应用的绝对路径："+PathTool.getWebRootPath());
	}

	@Test
	public void testGetWebInfPath() {
		System.out.println("获取WEB-INF的绝对路径:"+PathTool.getWebInfPath());
	}

	@Test
	public void testGetConfPath() {
		System.out.println("获取conf的绝对路径:"+PathTool.getConfPath());
	}

	@Test
	public void testGetClassPath() {
		System.out.println("获取web应用下的class路径绝对路径:"+PathTool.getClassPath());
	}
}
