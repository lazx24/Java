package com.test.number;

import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;

import com.coscon.shipsuite.common.util.generic.DecimalUtil;

public class DecimalUtilTest {

	@Test
	public void testDecimalTwoDouble() {
		System.out.println("保留两位小数点 最后一位四舍五入");
		double b=1234567.2172432424242;
		System.out.println("初始化double值:"+b);
		Double result=DecimalUtil.decimalTwo(b);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testDecimalDoubleInt() {
		System.out.println("自定义小数点位数 最后一位四舍五入");
		double b=1000.1274567890;
		System.out.println("初始化double值:"+b);
		double result=DecimalUtil.decimal(b, 4);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testNumberFormat() {
		System.out.println("String转换为String");
		String format="0.000";
		System.out.println("转换格式为:"+format);
		String strNum="123.45676767";
		System.out.println("初始化字符串为:"+strNum);
		String result=DecimalUtil.numberFormat(strNum, format);
		System.out.println("结果为："+result);
	}

	@Test
	public void testBigDecimalFormat() {
		System.out.println("String转换为自定义精度的bigDecimal");
		int mode=BigDecimal.ROUND_HALF_UP;
		System.out.println("转换模式为:BigDecimal.ROUND_HALF_UP");
		int scale=5;
		System.out.println("保留的精度位数为:"+scale);
		String strNum="123.45676767";
		System.out.println("初始化字符串为:"+strNum);
		BigDecimal result=DecimalUtil.bigDecimalFormat(strNum, scale, mode);
		System.out.println("结果为:"+result);
		
		System.out.println("取bigDecimal的绝对值");
		System.out.println("绝对值的结果为:"+DecimalUtil.abs(strNum));
		
		System.out.println("取bigDecimal反值");
		System.out.println("负值结果为:"+DecimalUtil.negate(strNum));
		
		System.out.println("将该值的小数点向左移动 n 位");
		int left=5;
		System.out.println("向左移动位数:"+left);
		System.out.println("结果为:"+DecimalUtil.movePointLeft(strNum, left));
		
		System.out.println("将该值的小数点向右移动 n 位");
		int right=5;
		System.out.println("向右移动位数:"+right);
		System.out.println("结果为:"+DecimalUtil.movePointRight(strNum, right));
	}

	@Test
	public void testAdd() {
		System.out.println("计算两个double值相加的结果");
		double v1=1234.123;
		System.out.println("第一个值:"+v1);
		double v2=1212.1243243;
		System.out.println("第二个值:"+v2);
		double result=DecimalUtil.add(v1,v2);
		System.out.println("相加的结果为:"+result);
	}

	@Test
	public void testSubtract() {
		System.out.println("计算两个double值相减的结果");
		double v1=1234.123;
		System.out.println("第一个值:"+v1);
		double v2=1212.124;
		System.out.println("第二个值:"+v2);
		double result=DecimalUtil.subtract(v1,v2);
		System.out.println("相减的结果为:"+result);
		
		double v3=DecimalUtil.add(result, v2);
		System.out.println(v3);
	}

	@Test
	public void testMultiply() {
		System.out.println("计算两个double值相乘的结果");
		double v1=1000;
		System.out.println("第一个值:"+v1);
		double v2=1212.124;
		System.out.println("第二个值:"+v2);
		double result=DecimalUtil.multiply(v1,v2);
		System.out.println("相乘的结果为:"+result);
	}

	@Test
	public void testDivideDoubleDouble() {
		System.out.println("计算两个double值相除的结果");
		double v1=1000;
		System.out.println("第一个值:"+v1);
		double v2=33;
		System.out.println("第二个值:"+v2);
		double result=DecimalUtil.divide(v1,v2);
		System.out.println("相除的结果为:"+result);
	}

	@Test
	public void testDivideDoubleDoubleInt() {
		System.out.println("计算两个double值相除的结果 自定义精度");
		double v1=1000;
		System.out.println("第一个值:"+v1);
		double v2=33;
		System.out.println("第二个值:"+v2);
		double result=DecimalUtil.divide(v1,v2,3);
		System.out.println("相除的结果为:"+result);
	}

	@Test
	public void testIsInvalidDouble() {
		System.out.println("验证double是否非法");
		double b=999999999;
		boolean result=DecimalUtil.isInvalidDouble(b);
		System.out.println("double值"+b+"是否合法"+result);
	}

	@Test
	public void testRound() {
		System.out.println("四舍五入");
		double b=1234.5678900;
		System.out.println("double的值为:"+b);
		int scale=4;
		System.out.println("精度保留几位:"+scale);
		double result=DecimalUtil.round(b, scale);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testGetRangeRandomIntInt() {
		System.out.println("生成固定范围的随机数");
		int min=10;
		int max=20;
		System.out.println("生成"+min+"到"+max+"范围的随机数");
		int result=DecimalUtil.getRangeRandom(min, max);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testGetRangeRandomInt() {
		System.out.println("生成固定范围的随机数");
		int max=20;
		System.out.println("生成小于"+max+"范围的随机数");
		int result=DecimalUtil.getRangeRandom(max);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testGetRandomNumIntInt() {
		
	}

	@Test
	public void testGetRandomNumInt() {
		System.out.println("生成固定长度的随机字符串");
		System.out.println(Double.toString(Math.random()));
		int length=20;
		System.out.println("生成"+length+"长度的字符串");
		String result=DecimalUtil.getRandomNum(length);
		System.out.println("结果为:"+result);
		System.out.println("结果字符串的长度为:"+result.length());
	}

	@Test
	public void testGetRandomLetter() {
		System.out.println("产生固定长度的随机字母");
		int length=50;
		System.out.println("产生"+length+"长度的大写字母");
		String result=DecimalUtil.getRandomLetter(length);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testGetRandomStrLowerCase() {
		System.out.println("产生固定长度的随机字母");
		int length=50;
		System.out.println("产生"+length+"长度的小写字母");
		String result=DecimalUtil.getRandomLetterLowerCase(length);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testGetOrderNumber() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRandom() {
		System.out.println("生成有前缀的随机数");
		String prefix="zoujianhua";
		int length=30;
		System.out.println("前缀为"+prefix+";长度为:"+length);
		String result=DecimalUtil.getRandomNum(prefix,length);
		System.out.println("结果为:"+result);
	}

	@Test
	public void testUserZero() {
		System.out.println("生成有前缀的随机数 后面全部补0");
		int prefix=12;
		int length=10;
		System.out.println("前缀为"+prefix+";长度为:"+length);
		String result=DecimalUtil.prefixZero(prefix,length);
		System.out.println("结果为:"+result);
		
		System.out.println("生成有后缀的随机数 前面全部补0");
		int suffix=12;
		int len=10;
		System.out.println("后缀为"+suffix+";长度为:"+len);
		String result2=DecimalUtil.suffixZero(suffix,len);
		System.out.println("结果为:"+result2);
	}

}
