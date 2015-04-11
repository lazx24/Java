package com.test.util.string;

import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.Test;

import com.coscon.shipsuite.common.util.generic.ArrayUtil;
import com.coscon.shipsuite.common.util.string.StringUtil;

public class StringUtilTest {

	@Test
	public void testReadInputStreamString() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadInputStream() {
		fail("Not yet implemented");
	}

	@Test
	public void testShortenStringInt() {
		System.out.println("返回多少长度的字符串(以..结尾)");
		String s="1bcdddddddddddddddd";
		System.out.println("初始化字符串:"+s);
		int length=3;
		System.out.println("保留长度为:"+length);
		String result=StringUtil.shorten(s, length);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testShortenStringIntString() {
		System.out.println("返回多少长度的字符串(自定义后缀)");
		String s="1bcdddddddddddddddd";
		System.out.println("初始化字符串:"+s);
		int length=3;
		System.out.println("保留长度为:"+length);
		String suffix=",,";
		System.out.println("后缀为:"+suffix);
		String result=StringUtil.shorten(s, length,suffix);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testReverse() {
		System.out.println("反转字符串");
		String s="abcdefg";
		System.out.println("初始化字符串:"+s);
		String result=StringUtil.reverse(s);
		System.out.println("结果为："+result);
	}

	@Test
	public void testIsChinese() {
		System.out.println("是否是中文");
		String s="中国";
		System.out.println("初始化字符串:"+s);
		boolean result=StringUtil.isChinese(s);
		System.out.println("结果为："+result);
	}

	@Test
	public void testRemoveSameString() {
		System.out.println("删除相同的字符串 此方法以空格分隔");
		String s="a b c d a a a a a a 中 国 中 中 国 人";
		System.out.println("初始化字符串为:"+s);
		String result=StringUtil.removeSameString(s);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testLength() {
		System.out.println("获取字符串字节长度");
		String s="abc中国人";
		System.out.println("初始化字符串为:"+s);
		int result=StringUtil.length(s);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testGetRandColorCode() {
		System.out.println("获取十六进制的颜色代码:"+StringUtil.getRandColorCode());
	}

	@Test
	public void testArrayCopy() {
		System.out.println("数组复制");
		String[] src={"1","2"};
		System.out.println("初始化的数组内容："+ArrayUtil.toString(src));
		String[] dest=new String[src.length];
		StringUtil.arrayCopy(src, dest);
		System.out.println("复制后数组内容为:"+ArrayUtil.toString(dest));
		
	}

	@Test
	public void testIsEmpty() {
		System.out.println("字符串是否为空");
		System.out.println(StringUtil.isNullOrEmpty(null));
	}

	@Test
	public void testIsNotEmpty() {
		System.out.println("字符串是否不为空");
		System.out.println(StringUtil.isNotNullAndNotEmpty(null));
	}

	@Test
	public void testIsBlank() {
		
	}

	@Test
	public void testIsNotBlank() {
		fail("Not yet implemented");
	}

	@Test
	public void testTrim() {
		fail("Not yet implemented");
	}

	@Test
	public void testTrimToEmpty() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsIgnoreCase() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfStringChar() {
		fail("Not yet implemented");
	}

	@Test
	public void testIndexOfStringCharInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testContains() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsIgnoreCase() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsAnyStringCharArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsAnyStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testBase64CodeByteTo64String() {
		fail("Not yet implemented");
	}

	@Test
	public void testBase64CodeDecode() {
		fail("Not yet implemented");
	}

	@Test
	public void testIntToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testStringToInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testStringToDobule() {
		fail("Not yet implemented");
	}

	@Test
	public void testDoubleToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testLongToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testStringToLong() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetEmailByString() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsLetter() {
		fail("Not yet implemented");
	}

	@Test
	public void testToLength() {
		fail("Not yet implemented");
	}

	@Test
	public void testEncoding() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecoding() {
		fail("Not yet implemented");
	}

	@Test
	public void testSplit() {
		fail("Not yet implemented");
	}

	@Test
	public void testReplace() {
		fail("Not yet implemented");
	}

	@Test
	public void testHtmlencode() {
		fail("Not yet implemented");
	}

	@Test
	public void testHtmldecode() {
		fail("Not yet implemented");
	}

	@Test
	public void testHtmlshow() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFirstPairIndex() {
		fail("Not yet implemented");
	}

	@Test
	public void testToHexString() {
		fail("Not yet implemented");
	}

	@Test
	public void testEnoughZero() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddStringRight() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddSubString() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddZeroRight() {
		fail("Not yet implemented");
	}

	@Test
	public void testCharCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testFormatDecimalDouble() {
		fail("Not yet implemented");
	}

	@Test
	public void testFormatDecimalDoubleIntInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testHtmlFilter() {
		fail("Not yet implemented");
	}

	@Test
	public void testHtmlFilterToEmpty() {
		fail("Not yet implemented");
	}

	@Test
	public void testIgnoreNull() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsNotAllowed4TextBox() {
		fail("Not yet implemented");
	}

	@Test
	public void testLinkFilter() {
		fail("Not yet implemented");
	}

	@Test
	public void testLtrimString() {
		fail("Not yet implemented");
	}

	@Test
	public void testLtrimStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testRtrimString() {
		fail("Not yet implemented");
	}

	@Test
	public void testRtrimStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testSqlWildcardFilter() {
		fail("Not yet implemented");
	}

	@Test
	public void testToCharSet() {
		fail("Not yet implemented");
	}

	@Test
	public void testHexString2Int() {
		fail("Not yet implemented");
	}

	@Test
	public void testHexString2Bytes() {
		fail("Not yet implemented");
	}

	@Test
	public void testIgnoreEnter() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnderline2Uppercase() {
		fail("Not yet implemented");
	}

	@Test
	public void testMain() {
		fail("Not yet implemented");
	}

}
