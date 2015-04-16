package com.test.util.string;

import org.junit.Test;

import com.common.util.generic.ArrayUtil;
import com.common.util.string.SpellUtils;

public class SpellUtilsTest {

	@Test
	public void testGetCnAscii() {
		System.out.println("获得单个汉字的ASCII");
		char a='中';
		System.out.println("单个汉字是:"+a);
		int ascii= SpellUtils.getCnAscii(a);
		System.out.println("中文汉字:"+a+"的ACSII码为:"+ascii);
	}

	@Test
	public void testGetSpellByAscii() {
		System.out.println("根据ASCII码到SpellMap中查找对应的拼音");
		System.out.println("获得单个汉字的ASCII");
		char a='中';
		System.out.println("单个汉字是:"+a);
		int ascii= SpellUtils.getCnAscii(a);
		System.out.println("中文汉字:"+a+"的ACSII码为:"+ascii);
		
		System.out.println("根据ASCII码到SpellMap中查找对应的拼音");
		String spell=SpellUtils.getSpellByAscii(ascii);
		System.out.println("拼音为:"+spell);
	}

	@Test
	public void testGetFullSpellString() {
		System.out.println("返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换");
		String str="我是中国人";
		System.out.println("初始化汉字为:"+str);
		String result=SpellUtils.getFullSpell(str);
		System.out.println("得到汉字"+str+"的全拼："+result);
	}

	@Test
	public void testGetFullSpellChar() {
		System.out.println("取得字符的中文拼音");
		char str='我';
		System.out.println("初始化汉字为:"+str);
		String result=SpellUtils.getFullSpell(str);
		System.out.println("得到汉字"+str+"的全拼："+result);
	}

	@Test
	public void testGetFirstSpellChar() {
		System.out.println("取得字符的中文拼音首字母");
		char str='我';
		System.out.println("初始化汉字为:"+str);
		String result=SpellUtils.getFirstSpell(str);
		System.out.println("得到汉字"+str+"的中文拼音首字母："+result);
	}

	@Test
	public void testGetFirstSpellString() {
		System.out.println("取得字符的中文拼音首字母");
		String str="我是中国人";
		System.out.println("初始化汉字为:"+str);
		String result=SpellUtils.getFirstSpell(str);
		System.out.println("得到汉字"+str+"的中文拼音首字母："+result);
	}

	@Test
	public void testGetAbbreviateSpell() {
		System.out.println("取得中文的拼音缩写，其中第一个为全拼，后面为首字母");
		String str="我是中国人";
		System.out.println("初始化汉字为:"+str);
		String result=SpellUtils.getAbbreviateSpell(str);
		System.out.println("得到汉字"+str+"的拼音缩写："+result);
		
	}

	@Test
	public void testGetSurnameFirstSpell() {
		System.out.println("取得姓的拼音首字母");
		String str="邹建华";
		String result=SpellUtils.getSurnameFirstSpell(str);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testGetSurnameFirstSpellRange() {
		System.out.println("取得一批姓名的姓的首字母字符串数组");
		String[] str={"邹建华","陈欢","秦真"};
		String[] result=SpellUtils.getSurnameFirstSpellRange(str);
		System.out.println("结果为:"+ArrayUtil.toString(result));
	}
}
