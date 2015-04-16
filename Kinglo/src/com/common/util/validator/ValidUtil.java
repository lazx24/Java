package com.common.util.validator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;

public class ValidUtil {
    
    /**
     * 判断是否是各种字母数字和符号
     */
    private static final String REGEX_SYMBOL = "^[A-Za-z0-9 `\\-=\\[\\]\\\\;',\\./~!@#\\$%\\^&\\*\\(\\)_\\+\\{\\}\\|:\\\"<>\\?\\r\\n]*$";
    
    /**
     * EXCEL正则表达式
     */
    private static final String REGEX_EXCEL = "^.+\\.(xls:xlsx)$";
    
    /**
     * 正整数正则表达式
     */
    private static final String REGEX_POSITIVE_INTEGER = "^[0-9]+$";
    
    /**
     * 浮点数正则表达式
     */
    private static final String REGEX_FLOAT_NUMBER = "^(\\+|-)[0-9]+\\.[0-9]+$";
    
    /**
     * 邮政编码的正则表达式
     */
    private static final String REGEX_POSTCODE = "^[1-9][0-9]{5}$";
    
    /**
     * 简单的手机号码的正则表达式
     */
    private static final String REGEX_SIMPLE_PHONE = "^1[\\d]{10}$";
    
    /**
     * 中文正则表达式
     */
    private static final String REGEX_CHINESE = "^[\u0391-\uFFE5]+$";
    
    /**
     * 简体中文的正则表达式。
     */
    private static final String REGEX_SIMPLE_CHINESE = "^[\u4E00-\u9FA5]+$";

    /**
     * 字母数字的正则表达式。
     */
    private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]+";
    
    /**
     * 整数正则表达式
     */
    private static final String REGEX_INTEGER = "^[\\+|-]\\d+$";

    /**
     * 移动手机号码的正则表达式。
     */
    private static final String REGEX_CHINA_MOBILE = "1(3[4-9]|4[7]|5[012789]|8[278])\\d{8}";

    /**
     * 联通手机号码的正则表达式。
     */
    private static final String REGEX_CHINA_UNICOM = "1(3[0-2]|5[56]|8[56])\\d{8}";

    /**
     * 电信手机号码的正则表达式。
     */
    private static final String REGEX_CHINA_TELECOM = "(?!00|015|013)(0\\d{9,11})|(1(33|53|80|89)\\d{8})";

    /**
     * 整数或浮点数的正则表达式。
     */
    private static final String REGEX_NUMERIC = "(\\+|-){0,1}(\\d+)([.]?)(\\d*)";

    /**
     * 身份证号码的正则表达式。
     */
    private static final String REGEX_ID_CARD = "(\\d{14}|\\d{17})(\\d|x|X)";

    /**
     * 电子邮箱的正则表达式。
     */
    private static final String REGEX_EMAIL = "^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]{2}|net|NET|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|cc|CC)$";

    
    /**
     * 匹配方法
     * @param regex  	正则表达式
     * @param value	匹配的值
     * @return		结果
     */
    public static boolean matches(String regex,String value){
	Pattern pattern = Pattern.compile(regex);
	Matcher matcher = pattern.matcher(value);
	return matcher.matches();
    }
    
    /**
     * 验证此字符串是否是手机号码
     * @param mobileNo  手机号码
     * @return		boolean	
     */
    public static boolean isMobileNo(String mobileNo) {
	return matches(REGEX_SIMPLE_PHONE,mobileNo);
    }

    /**
     * 验证此字符串是否是邮政编码
     * @param postalCode 	邮政编码
     * @return			boolean
     */
    public static boolean isPostCode(String postCode) {
	return matches(REGEX_POSTCODE,postCode);
    }

    /**
     * 验证是否是Excel文件
     * @param fileName	文件名
     * @return		boolean
     */
    public static boolean isExcel(String fileName) {
	return matches(REGEX_EXCEL,fileName);
    }


    /**
     * 验证此字符串是否是正整数
     * @param positiveNumber 	正整数的字符串
     * @return			boolean
     */
    public static boolean isPositiveNumber(String positiveNumber) {
	return matches(REGEX_POSITIVE_INTEGER,positiveNumber);
    }

    /**
     * 判断是否是图片(根据后缀名不靠谱)
     * @param fileName 	文件名
     * @return		boolean
     */
    public static boolean isImage(String fileName) {
	BufferedImage bi;
	try {
	    bi = ImageIO.read(new File(fileName));
	    if (bi == null) {
		System.out.println(fileName+"不是图片!");
		return false;
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * 是否是质数
     * @param x 数字
     * @return  boolean
     */
    public static boolean isPrime(int x) {
	if (x <= 7) {
	    if (x == 2 || x == 3 || x == 5 || x == 7)
		return true;
	}
	int c = 7;
	if (x % 2 == 0)
	    return false;
	if (x % 3 == 0)
	    return false;
	if (x % 5 == 0)
	    return false;
	int end = (int) Math.sqrt(x);
	while (c <= end) {
	    if (x % c == 0) {
		return false;
	    }
	    c += 4;
	    if (x % c == 0) {
		return false;
	    }
	    c += 2;
	    if (x % c == 0) {
		return false;
	    }
	    c += 4;
	    if (x % c == 0) {
		return false;
	    }
	    c += 2;
	    if (x % c == 0) {
		return false;
	    }
	    c += 4;
	    if (x % c == 0) {
		return false;
	    }
	    c += 6;
	    if (x % c == 0) {
		return false;
	    }
	    c += 2;
	    if (x % c == 0) {
		return false;
	    }
	    c += 6;
	}
	return true;
    }

    /**
     * 是否是浮点数
     * @param number	数值字符串
     * @return		boolean
     */
    public static boolean isDouble(String number) {
	return matches(REGEX_FLOAT_NUMBER,number);
    }

    /**
     * 判断是否是整数
     * @param number	数值字符串
     * @return		boolean
     */
    public static boolean isInteger(String number) {
	return matches(REGEX_INTEGER,number);
    }
    
    /**
     * 判断对象是否是空
     * @param object	Object对象
     * @return		boolean
     */
    public static boolean isEmpty(Object object) {
	return object == null ? true : false;
    }

    /**
     * 判断字符串是否数字和字母
     * @param number	数字字母的字符串
     * @return		boolean
     */
    public static boolean isAlphanumeric(String number) {
	return matches(REGEX_ALPHANUMERIC,number);
    }

    /**
     * 是否为中国移动手机号码
     * @param mobileNo	手机号码
     * @return		boolean
     */
    public static boolean isChinaMobile(String mobileNo) {
	return matches(REGEX_CHINA_MOBILE,mobileNo);
    }

    /**
     * 是否为中国联通手机号码
     * @param mobileNo	手机号码
     * @return		boolean
     */
    public static boolean isChinaUnicom(String mobileNo) {
	return matches(REGEX_CHINA_UNICOM,mobileNo);
    }

    /**
     * 判断是否为电信手机号码
     * @param mobileNo	手机号码
     * @return		boolean
     */
    public static boolean isChinaTelecom(String mobileNo) {
	return matches(REGEX_CHINA_TELECOM,mobileNo);
    }

    /**
     * 判断是否是合法的邮箱地址
     * @param email	邮箱地址
     * @return		boolean
     */
    public static boolean isEmail(String email) {
	return matches(REGEX_EMAIL,email);
    }
    
    /**
     * 判断是否是身份证号码
     * @param idCard	身份证号码
     * @return		boolean
     */
    public static boolean isIdCard(String idCard) {
	// 15位或18数字, 14数字加x(X)字符或17数字加x(X)字符才是合法的
	return matches(REGEX_ID_CARD,idCard);
    }

    /**
     * 是否是手机号码
     * @param mobileNo	手机号码
     * @return		boolean
     */
    public static boolean isMobile(String mobileNo) {
	return isChinaMobile(mobileNo) || isChinaUnicom(mobileNo) || isChinaTelecom(mobileNo);
    }


    /**
     * 是否是固定范围内的整数数字的字符串
     * @param number	数值
     * @param min	最小值
     * @param max	最大值
     * @return 		true/false
     */
    public static boolean isNumber(String number, int min, int max) {
	if(isInteger(number)){
	    int num = Integer.parseInt(number);
	    return num >= min && num <= max;
	}
	return false;
    }

    /**
     * 判断字符是否为整数或浮点数
     * @param str
     * @return
     */
    public static boolean isNumeric(String numberic) {
	return matches(REGEX_NUMERIC,numberic);
    }

    /**
     * 判断字符是否为符合精度要求的整数或浮点数
     * @param number		字符串
     * @param fractionNum	精度
     * @return
     */
    public static boolean isNumeric(String number, int fractionNum) {
	if (isEmpty(number)) {
	    return false;
	}

	String regex = "(\\+|-){0,1}(\\d+)([.]?)(\\d{0," + fractionNum + "})";
	return matches(regex,number);
    }

    /**
     * 是否是简体中文字符串
     * @param simpleChinese 简体中文字符串
     * @return		    boolean	
     */
    public static boolean isSimpleChinese(String simpleChinese) {
	return matches(REGEX_SIMPLE_CHINESE,simpleChinese);
    }

    
    /**
     * 判断单个字符是否是大写
     * @param c	单个字符
     * @return	boolean
     */
    public static boolean isUpperCase(Character c) {
	return Character.isUpperCase(c);
    }
    
    /**
     * 判断是否是数字和字母以及各种特殊符号
     * @param symbo 各种符号
     * @return	    boolean
     */
    public static boolean isEnNumSymbol(String symbol) {
	return matches(REGEX_SYMBOL,symbol);
    }
    
    /**
     * 判断是否是中文 包含简体和繁体
     * @param chinese
     * @return
     */
    public static boolean isChinese(String chinese){
	return matches(REGEX_CHINESE,chinese);
    }
    
    /**
     * 判断是否是正常的日期字符串
     * @param date
     * @return
     */
    private static boolean isNormalDateFormat(String date){
	return false;
    }
}
