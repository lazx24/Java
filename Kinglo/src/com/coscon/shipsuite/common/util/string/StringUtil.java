package com.coscon.shipsuite.common.util.string;

import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Clob;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.coscon.shipsuite.common.exception.ShipSuiteRuntimeException;
import com.coscon.shipsuite.common.util.compare.PairKeyword;
import com.coscon.shipsuite.common.util.compare.PairKeywordComparator;
import com.coscon.shipsuite.common.util.validator.ValidUtil;

public final class StringUtil {
    private static class InstanceHolder {
	static StringUtil instance = new StringUtil();
    }

    public static StringUtil getInstance() {
	return InstanceHolder.instance;
    }

    private static NumberFormat numberFormatter = NumberFormat
	    .getNumberInstance();
    public static final String DEFAULT_SPLIT_SYBMBOL = ",";
    static final char DBC_CHAR_START = '!';
    static final char DBC_CHAR_END = '~';
    static final char SBC_CHAR_START = '！';
    static final char SBC_CHAR_END = '～';
    static final int CONVERT_STEP = 65248;
    static final char SBC_SPACE = '　';
    static final char DBC_SPACE = ' ';
    
    /**
     * 字符串所有字符转换为全拼
     * @param src
     * @return
     */
    public static String bj2qj(String src) {
	if (src == null) {
	    return src;
	}
	StringBuilder buf = new StringBuilder(src.length());
	char[] ca = src.toCharArray();
	for (int i = 0; i < ca.length; i++) {
	    if (ca[i] == ' ') {
		buf.append('　');
	    } else if ((ca[i] >= '!') && (ca[i] <= '~')) {
		buf.append((char) (ca[i] + 65248));
	    } else {
		buf.append(ca[i]);
	    }
	}
	return buf.toString();
    }
    
    /**
     * 字节数组转换为字符串
     * @param bytes 字节数组
     * @return
     */
    public static String bytesToString(byte[] bytes) {
	return bytesToString(bytes, "UTF-8");
    }
    
    /**
     * 字节数组转换为字符串
     * @param bytes	字节数组
     * @param charset	字符编码
     * @return
     */
    public static String bytesToString(byte[] bytes, String charset) {
	try {
	    return new String(bytes, charset);
	} catch (UnsupportedEncodingException e) {
	}
	return null;
    }
    
    /**
     * 是否能转换为Integer
     * @param inputValue 输入字符串
     * @return
     */
    public static boolean canParseToInt(String inputValue) {
	try {
	    Integer.parseInt(inputValue);
	    return true;
	} catch (NumberFormatException localNumberFormatException) {
	}
	return false;
    }
    
    /**
     * 返回固定长度的字符串
     * @param string 	输入字符串
     * @param length    返回字符串的长度
     * @return
     */
    public static String changeLength(String string, int length) {
	if (string.length() > length) {
	    string = string.substring(0, length);
	}
	while (string.length() < length) {
	    string = string + " ";
	}
	return string;
    }
    
    /**
     * 左边添加字符
     * @param string 	输入字符串
     * @param length	返回字符串长度
     * @param strInsert	左边插入的字符串
     * @return
     */
    public static String changeLengthLeft(String string, int length,
	    String strInsert) {
	if (string.length() > length) {
	    return string;
	}
	while (string.length() < length) {
	    string = strInsert + string;
	}
	return string;
    }
    
    /**
     * 是否包含换行符\n
     * @param text	输入字符串
     * @return
     */
    public static boolean containsLF(String text) {
	if (isNullOrEmpty(text)) {
	    return false;
	}
	Pattern crlf = Pattern.compile("\n");
	Matcher matcher = crlf.matcher(text);
	return matcher.find();
    }
    
    /**
     * 转换为List列表中的Object数组为String数组
     * @param objectArrayList List列表Object数组
     * @return
     */
    public static List<String[]> convertObjectArrayToStringArray(
	    List<Object[]> objectArrayList) {
	List<String[]> strArryList = new ArrayList<String[]>(objectArrayList.size());
	for (Object[] objArray : objectArrayList) {
	    int len = Array.getLength(objArray);
	    String[] strArry = new String[len];
	    for (int i = 0; i < len; i++) {
		strArry[i] = objArray[i].toString();
	    }
	    strArryList.add(strArry);
	}
	return strArryList;
    }

    public static String correctCRLF(String text) {
	if (isNullOrEmpty(text)) {
	    return text;
	}
	Pattern crlf = Pattern.compile("\n");
	Matcher matcher = crlf.matcher(text);
	if (matcher.find()) {
	    String newText = matcher.replaceAll("\r\n");
	    crlf = Pattern.compile("\r\r");
	    matcher = crlf.matcher(newText);
	    if (matcher.find()) {
		newText = matcher.replaceAll("\r");
	    }
	    return newText;
	}
	return text;
    }
    
    /**
     * 计算换行符\n的个数
     * @param text 输入的字符串
     * @return
     */
    public static int countLF(String text) {
	if (isNullOrEmpty(text)) {
	    return 0;
	}
	Pattern crlf = Pattern.compile("\n");
	Matcher matcher = crlf.matcher(text);
	int count = 0;
	while (matcher.find()) {
	    count++;
	}
	return count;
    }
    
    /**
     * Date转换为String(yyyy-MM-dd HH:mm:ss)
     * @param date 日期时间
     * @return
     */
    public static String dateLongStringFromDate(Date date) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String dateStr = sdf.format(date);
	return dateStr;
    }
    
    /**
     * Date转换为String(yyyy-MM-dd)
     * @param date 日期时间
     * @return
     */
    public static String dateShortStringFromDate(Date date) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String dateStr = sdf.format(date);
	dateStr = dateStr.substring(0, 10);
	return dateStr;
    }
    
    /**
     * Date转换为String(yyyy-MM-dd)
     * @param date 日期时间
     * @return
     */
    public static String dateStringFromDate(Date date) {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String dateStr = sdf.format(date);
	dateStr = dateStr.substring(0, 10);
	return dateStr;
    }
    
    /**
     * 获取第一个空格的位置
     * @param line
     * @return
     */
    public static int firstNoneSpaceChar(String line) {
	int position = -1;
	for (int i = 0; i < line.length(); i++) {
	    if (line.charAt(i) != ' ') {
		position = i;
		break;
	    }
	}
	return position;
    }
    
    /**
     * 以空格填充字符串到固定长度
     * @param string	输入字符串
     * @param len	固定长度
     * @return
     */
    public static final String fixLength(String string, int len) {
	int stringLen = length(string);
	if (stringLen > len) {
	    return string;
	}
	int fixLen = len - stringLen;
	StringBuffer sb = new StringBuffer();
	while (fixLen-- > 0) {
	    sb.append(" ");
	}
	string = string + sb.toString();
	return string;
    }
    
    /**
     * 获取字符串字节长度
     * @param str 输入的字符串
     * @return
     */
    public static int length(String str) {
	int length = 0;
	String chiness = "[\u4e00-\u9fa5]";

	for (int i = 0; i < str.length(); i++) {
	    String temp = str.substring(i, i + 1);

	    if (temp.matches(chiness)) {
		length += 2;
	    } else {
		length += 1;
	    }
	}

	return length;
    }
    
    /**
     * Number转换为String
     * @param number		输入的数
     * @param numberDigit	保留几位小数点
     * @return
     */
    public static String formatNumber(long number, int numberDigit) {
	numberFormatter.setMaximumIntegerDigits(numberDigit);
	numberFormatter.setMinimumIntegerDigits(numberDigit);
	numberFormatter.setGroupingUsed(false);
	numberFormatter.setMaximumFractionDigits(0);
	return numberFormatter.format(number);
    }
    
    /**
     * 转换Number
     * @param number      Object对象
     * @param dataFormat  转换格式	
     * @return
     */
    public static String formatNumber(Object number, String dataFormat) {
	if (number == null) {
	    return "";
	}
	if (isNullOrEmptyWithTrim(dataFormat)) {
	    return number.toString();
	}
	DecimalFormat format = new DecimalFormat(dataFormat);
	if ((number instanceof Double)) {
	    if (Double.isNaN(((Double) number).doubleValue())) {
		number = new Double("0.00");
	    }
	    return format.format(number);
	}
	if ((number instanceof Float)) {
	    if (Float.isNaN(((Float) number).floatValue())) {
		number = new Float("0.00");
	    }
	    return format.format(number);
	}
	if ((number instanceof Long)) {
	    if (((Long) number).equals(Long.valueOf(-9223372036854775808L))) {
		number = "";
	    }
	    return format.format(number);
	}
	if ((number instanceof Integer)) {
	    if (((Integer) number).equals(Integer.valueOf(-2147483648))) {
		number = "";
	    }
	    return format.format(number);
	}
	if ((number instanceof BigDecimal)) {
	    return format.format(number);
	}
	return number.toString();
    }
    
    /**
     * 获取ClassPath
     * @param cls
     * @return
     */
    public static String getClassPath(Class<?> cls) {
	String path = cls.getName();
	int index = path.lastIndexOf(".");
	if (index < 0) {
	    return path;
	}
	return path.substring(0, index);
    }
    
    /**
     * Clob转换为字符串
     * @param clob
     * @return
     */
    public static String getClobString(Clob clob) {
	if (clob == null) {
	    return null;
	}
	try {
	    Reader inStreamDoc = clob.getCharacterStream();

	    char[] tempDoc = new char[(int) clob.length()];
	    inStreamDoc.read(tempDoc);
	    inStreamDoc.close();
	    return new String(tempDoc);
	} catch (Exception e) {
	    throw new ShipSuiteRuntimeException(e);
	}
    }
    
    /**
     * 获取任何对象的字符串形式
     * @param obj Object对象
     * @return
     */
    public static String getString(Object obj) {
	if (obj == null) {
	    return "";
	}
	return obj.toString();
    }
    
    /**
     * 得到去空格后的字符串
     * @param s 输入的字符串
     * @return
     */
    public static String getTrimString(String s) {
	if (s != null) {
	    return s.trim();
	}
	return null;
    }
    
    /**
     * 将HTML中的特殊字符转换为Text
     * @param html
     * @return
     */
    public static String html2text(String html) {
	if (isNullOrEmpty(html)) {
	    return html;
	}
	String dst = html;
	dst = replaceAll(dst, "&lt", "<");
	dst = replaceAll(dst, "&gt;", ">");
	dst = replaceAll(dst, " ", "");
	dst = replaceAll(dst, "<br>", "\n");
	dst = replaceAll(dst, "<br/>", "\n");
	dst = replaceAll(dst, "&nbsp;", " ");
	dst = replaceAll(dst, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
	return dst;
    }
    
    /**
     * 比较两个字符串是否相等
     * @param string1  	第一个字符串
     * @param string2	第二个字符串
     * @return
     */
    public static boolean isEqual(String string1, String string2) {
	if (string1 == string2) {
	    return true;
	}
	if ((string1 == null) || (string2 == null)) {
	    return false;
	}
	return string1.equals(string2);
    }
    
    /**
     * 去除空格忽略大小写比较两个字符串是否相等
     * @param string1	第一个字符串
     * @param string2	第二个字符串
     * @return
     */
    public static boolean isEqualIgnoreCaseWithTrim(String string1,
	    String string2) {
	if (string1 == string2) {
	    return true;
	}
	if ((string1 == null) || (string2 == null)) {
	    return false;
	}
	return string1.trim().equalsIgnoreCase(string2.trim());
    }
    
    /**
     * 去除空格后比较两个字符串是否相等
     * @param string1	第一个字符串
     * @param string2	第二个字符串
     * @return
     */
    public static boolean isEqualWithTrim(String string1, String string2) {
	if (string1 == string2) {
	    return true;
	}
	if ((string1 == null) || (string2 == null)) {
	    return false;
	}
	return string1.trim().equals(string2.trim());
    }
    
    /**
     * 字符串是否为空
     * @param string 输入的字符串
     * @return
     */
    public static boolean isNotNullAndNotEmpty(String string) {
	return !isNullOrEmpty(string);
    }
    
    /**
     * 去除空格后字符串是否不为空
     * @param string	输入的字符串
     * @return
     */
    public static boolean isNotNullAndNotEmptyWithTrim(String string) {
	return !isNullOrEmptyWithTrim(string);
    }
    
    /**
     * 如果第一个字符串为NULL 返回第二个字符串
     * @param string	第一个字符串
     * @param otherString第二个字符串
     * @return
     */
    public static String isNull(String string, String otherString) {
	if (string == null) {
	    return otherString;
	}
	return string;
    }
    
    /**
     * 字符串是否为空
     * @param string 输入的字符串
     * @return
     */
    public static boolean isNullOrEmpty(String string) {
	return (string == null) || (string.isEmpty());
    }
    
    /**
     * 去除空格后字符串是否为空
     * @param string 输入的字符串
     * @return
     */
    public static boolean isNullOrEmptyWithTrim(String string) {
	return (string == null) || (string.trim().isEmpty());
    }
    
    /**
     * 是否是数字型的字符串
     * @param inputMessage 输入的字符串
     * @return
     */
    public static boolean isNumericString(String inputMessage) {
	if (isNullOrEmpty(inputMessage)) {
	    return false;
	}
	return Pattern.matches("[\\d]+[.]?[\\d]+", inputMessage);
    }
    
    /**
     * 全字符转换为半字符
     * @param src 输入的全字符
     * @return
     */
    public static String qj2bj(String src) {
	if (src == null) {
	    return src;
	}
	StringBuilder buf = new StringBuilder(src.length());
	char[] ca = src.toCharArray();
	for (int i = 0; i < src.length(); i++) {
	    if ((ca[i] >= 65281) && (ca[i] <= 65374)) {
		buf.append((char) (ca[i] - 65248));
	    } else if (ca[i] == '　') {
		buf.append(' ');
	    } else {
		buf.append(ca[i]);
	    }
	}
	return buf.toString();
    }
    
    /**
     * 替换所有字符串
     * @param text	输入的字符串
     * @param oldValue	OldValue
     * @param newValue	NewValue
     * @return
     */
    public static String replaceAll(String text, String oldValue,
	    String newValue) {
	StringBuffer sb = new StringBuffer(text);
	int index = -1;
	while ((index = sb.indexOf(oldValue)) != -1) {
	    sb.replace(index, index + oldValue.length(), newValue);
	}
	return sb.toString();
    }
    
    /**
     * 字符串转换为字节数组
     * @param string 输入的字符串
     * @return
     */
    public static byte[] stringToBytes(String string) {
	return stringToBytes(string, "UTF-8");
    }
    
    /**
     * 字符串转换为字节数组
     * @param string	输入的字符串
     * @param charset	字符编码
     * @return
     */
    public static byte[] stringToBytes(String string, String charset) {
	try {
	    return string.getBytes(charset);
	} catch (UnsupportedEncodingException e) {
	}
	return null;
    }
    
    /**
     * 将Text中的特殊字符转换为HTML
     * @param text	输入的字符串
     * @return
     */
    public static String text2html(String text) {
	if (isNullOrEmpty(text)) {
	    return text;
	}
	String dst = text;
	dst = replaceAll(dst, "<", "&lt");
	dst = replaceAll(dst, ">", "&gt;");
	dst = replaceAll(dst, "\r\n", "<br>");
	dst = replaceAll(dst, "\r", "<br>");
	dst = replaceAll(dst, "\n", "<br>");
	dst = replaceAll(dst, " ", "&nbsp;");
	dst = replaceAll(dst, "\t", "&nbsp;&nbsp;&nbsp;&nbsp;");

	return dst;
    }
    
    /**
     * 将Throwable异常转换为字符串形式
     * @param error	Throwable异常
     * @return
     */
    public static String throwableToString(Throwable error) {
	if (error == null) {
	    return null;
	}
	StringBuffer sb = new StringBuffer();
	sb.append("\nErrorMessage: ");
	sb.append(error.getMessage() == null ? error : error.getMessage());
	sb.append("\nCause: ");
	sb.append(error.getCause());
	sb.append("\nStackTrace:");
	StackTraceElement[] steList = error.getStackTrace();
	if (steList != null) {
	    for (StackTraceElement ste : steList) {
		sb.append(ste.toString());
		sb.append("\n");
	    }
	}
	return sb.toString();
    }
    
    /**
     * 根据符号,;来分割字符串
     * @param string 	输入字符串
     * @return
     */
    public static List<String> toList(String string) {
	if (isNullOrEmptyWithTrim(string)) {
	    return new ArrayList<String>(0);
	}
	string = string.replaceAll(";", " ").replaceAll(",", " ").trim();
	String[] stringArray = string.split(" ");
	List<String> list = new ArrayList<String>(stringArray.length);
	for (String s : stringArray) {
	    if (isNotNullAndNotEmptyWithTrim(s)) {
		list.add(s);
	    }
	}
	return list;
    }
    
    /**
     * 分割字符串
     * @param string		输入字符串
     * @param splitSymbol	分割符号
     * @return
     */
    public static List<String> toList(String string, String splitSymbol) {
	if (isNullOrEmptyWithTrim(string)) {
	    return new ArrayList<String>(0);
	}
	String[] stringArray = string.trim().split(splitSymbol);
	List<String> list = new ArrayList<String>(stringArray.length);
	for (String s : stringArray) {
	    if (isNotNullAndNotEmptyWithTrim(s)) {
		list.add(s);
	    }
	}
	return list;
    }
    
    /**
     * 根据符号,;分割字符串
     * @param string	输入的字符串
     * @return
     */
    public static String[] toArray(String string) {
	if (isNullOrEmptyWithTrim(string)) {
	    return new String[0];
	}
	List<String> list = toList(string);

	return (String[]) list.toArray(new String[list.size()]);
    }
    
    /**
     * 转换为小写
     * @param s
     * @return
     */
    public static String toLowerCase(String s) {
	if (isNotNullAndNotEmpty(s)) {
	    return s.toLowerCase();
	}
	return s;
    }
    
    /**
     * 显示Collection的字符串形式
     * @param list
     * @return
     */
    public static String toString(Collection<?> list) {
	if (list != null) {
	    List<String> stringList = new ArrayList<String>(list.size());
	    for (Object obj : list) {
		stringList.add(obj.toString());
	    }
	    return toString((String[]) stringList.toArray(new String[stringList
		    .size()]));
	}
	return null;
    }
    
    /**
     * 显示为字符串形式
     * @param list		Collection列表
     * @param splitSymbol	分割符号
     * @return
     */
    public static String toString(Collection<?> list, String splitSymbol) {
	if (list != null) {
	    List<String> stringList = new ArrayList<String>(list.size());
	    for (Object obj : list) {
		stringList.add(obj.toString());
	    }
	    return toString((String[]) stringList.toArray(new String[stringList
		    .size()]), splitSymbol);
	}
	return null;
    }
    
    /**
     * 显示为字符串形式
     * @param array	字符串数组
     * @return
     */
    public static String toString(String[] array) {
	return toString(array, ",");
    }
    
    /**
     * 显示为字符串形式
     * @param array		字符串数组
     * @param splitSymbol	分割符号
     * @return
     */
    public static String toString(String[] array, String splitSymbol) {
	StringBuffer sb = new StringBuffer();
	if (array != null) {
	    String[] arrayOfString = array;
	    int j = array.length;
	    for (int i = 0; i < j; i++) {
		String s = arrayOfString[i];
		if (sb.length() > 0) {
		    sb.append(splitSymbol);
		}
		sb.append(s);
	    }
	    return sb.toString();
	}
	return null;
    }
    
    /**
     * 转换为大写
     * @param s	输入的字符串
     * @return
     */
    public static String toUpperCase(String s) {
	if (isNotNullAndNotEmpty(s)) {
	    return s.toUpperCase();
	}
	return s;
    }
    
    /**
     * 去除空格
     * @param s
     * @return
     */
    public static String trim(String s) {
	if (isNullOrEmpty(s)) {
	    return s;
	}
	return s.trim();
    }
    
    /**
     * 去除左边的空格
     * @param s
     * @return
     */
    public static String trimLeft(String s) {
	if (isNullOrEmpty(s)) {
	    return s;
	}
	while ((s.indexOf(" ") == 0) && (s.length() > 1)) {
	    s = s.substring(1);
	}
	if (s.indexOf(" ") == 0) {
	    return "";
	}
	return s;
    }
    
    /**
     * 去除右边的空格
     * @param s
     * @return
     */
    public static String trimRight(String s) {
	if (isNullOrEmpty(s)) {
	    return s;
	}
	while ((s.lastIndexOf(" ") == s.length() - 1) && (s.length() > 1)) {
	    s = s.substring(0, s.length() - 1);
	}
	if (s.lastIndexOf(" ") == 0) {
	    return "";
	}
	return s;
    }
    
    /**
     * 返回固定长度的字符串(以..结尾)
     * @param str	输入字符串
     * @param length	固定长度
     * @return
     */
    public static String shorten(String str, int length) {
	return shorten(str, length, "..");
    }

    /**
     * 返回固定长度的字符串(自定义后缀)
     * @param str	输入字符串
     * @param length	固定长度
     * @return
     */
    public static String shorten(String str, int length, String suffix) {
	if (str == null || suffix == null) {
	    return null;
	}
	if (str.length() > length) {
	    str = str.substring(0, length) + suffix;
	}
	return str;
    }

    /**
     * 反转字符串
     * @param str	输入的字符串
     * @return
     */
    public static String reverse(String str) {
	if (isNotNullAndNotEmpty(str)) {
	    char[] c = str.toCharArray();
	    char[] reverse = new char[c.length];
	    for (int i = 0; i < c.length; i++) {
		reverse[i] = c[c.length - i - 1];
	    }
	    return new String(reverse);
	}
	return null;
    }

    /**
     * 是否是中文
     * @param str 输入的字符串
     * @return
     */
    public static boolean isChinese(String str) {
	Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
	return pattern.matcher(str).matches();
    }
    
    /**
     * 删除相同的字符串
     * @param str	输入的字符串
     * @return
     */
    public static String removeSameString(String str) {
	Set<String> mLinkedSet = new LinkedHashSet<String>();// set集合的特征：其子集不可以重复
	String[] strArray = str.split(" ");// 根据空格(正则表达式)分割字符串
	StringBuffer sb = new StringBuffer();

	for (int i = 0; i < strArray.length; i++) {
	    if (!mLinkedSet.contains(strArray[i])) {
		mLinkedSet.add(strArray[i]);
		sb.append(strArray[i] + " ");
	    }
	}
	return sb.toString();
    }
    
    /**
     * 获取十六进制的颜色代码.例如 "#6E36B4" , For HTML ,
     * @return String
     */
    public static String getRandColorCode() {
	String r;
	String g;
	String b;
	Random random = new Random();
	r = Integer.toHexString(random.nextInt(256)).toUpperCase();
	g = Integer.toHexString(random.nextInt(256)).toUpperCase();
	b = Integer.toHexString(random.nextInt(256)).toUpperCase();
	System.out.println(r);
	System.out.println(g);
	System.out.println(b);
	r = (r.length() == 1) ? ("0" + r) : r;
	g = (g.length() == 1) ? ("0" + g) : g;
	b = (b.length() == 1) ? ("0" + b) : b;

	return r + g + b;
    }

    /**
     * 数组复制 比for效率高很多
     * @param src
     * @param dest
     */
    public static void arrayCopy(Object[] src, Object[] dest) {
	System.arraycopy(src, 0, dest, 0, dest.length);
    }
    
    /**
     * 包含任何一个
     * @param str
     * @param searchChars
     * @return
     */
    public static boolean containsAny(String str, char[] searchChars) {
	if ((str == null) || (str.length() == 0) || (searchChars == null)
		|| (searchChars.length == 0)) {
	    return false;
	}

	for (int i = 0; i < str.length(); i++) {
	    char ch = str.charAt(i);

	    for (int j = 0; j < searchChars.length; j++) {
		if (searchChars[j] == ch) {
		    return true;
		}
	    }
	}

	return false;
    }

    /**
     * 包含任何一个
     * @param str
     * @param searchChars
     * @return
     */
    public static boolean containsAny(String str, String searchChars) {
	if (searchChars == null) {
	    return false;
	}

	return containsAny(str, searchChars.toCharArray());
    }

    /**
     * String转Int
     * @param str
     * @return
     */
    public static int stringToInt(String str) {
	if (!isNullOrEmpty(str)) {
	    return Integer.parseInt(str);
	}
	return -1;
    }

    /**
     * String转Double
     * @param str
     * @return
     */
    public static double stringToDobule(String str) {
	if (!isNullOrEmpty(str)) {
	    return Double.parseDouble(str);
	}
	return -1d;
    }

    /**
     * double转String
     * @param d
     * @return
     */
    public static String doubleToString(double d) {
	return String.valueOf(d);
    }

    /**
     * long转String
     * @param l
     * @return
     */
    public static String longToString(long l) {
	return String.valueOf(l);
    }

    /**
     * String转long
     * @param str
     * @return
     */
    public static long stringToLong(String str) {
	if (!isNullOrEmpty(str)) {
	    return Long.parseLong(str);
	}
	return -1l;
    }
    
    /**
     * 从String中查找Email地址
     * @param content
     * @return
     */
    public static String getEmailByString(String content) {
	String email = null;
	if (content == null || content.length() < 1) {
	    return email;
	}
	// 找出含有@
	int beginPos;
	int i;
	String token = "@";
	String preHalf = "";
	String sufHalf = "";

	beginPos = content.indexOf(token);
	if (beginPos > -1) {
	    // 前项扫描
	    String s = null;
	    i = beginPos;
	    while (i > 0) {
		s = content.substring(i - 1, i);
		if (isLetter(s))
		    preHalf = s + preHalf;
		else
		    break;
		i--;
	    }
	    // 后项扫描
	    i = beginPos + 1;
	    while (i < content.length()) {
		s = content.substring(i, i + 1);
		if (isLetter(s))
		    sufHalf = sufHalf + s;
		else
		    break;
		i++;
	    }
	    // 判断合法性
	    email = preHalf + "@" + sufHalf;
	    if (ValidUtil.isEmail(email)) {
		return email;
	    }
	}
	return null;
    }
    
    /**
     * 判断是不是合法字符
     * @param str
     * @return
     */
    public static boolean isLetter(String str) {
	if (str == null || str.length() < 0) {
	    return false;
	}
	Pattern pattern = Pattern.compile("[\\w\\.-_]*");
	return pattern.matcher(str).matches();
    }
    
    /**
     * 把一个字节数组转换为16进制表达的字符串
     * @param bytes	字节数组
     * @return
     */
    public static String toHexString(byte[] bytes) {
	StringBuilder hexString = new StringBuilder();

	for (int i = 0; i < bytes.length; i++) {
	    hexString
		    .append(enoughZero(Integer.toHexString(bytes[i] & 0xff), 2));
	}
	return hexString.toString();
    }

    /**
     * 在字符串左边补齐0直到长度等于固定长度
     * @param str	输入字符串
     * @param len	固定长度
     * @return
     */
    public static String enoughZero(String str, int len) {
	while (str.length() < len) {
	    str = "0" + str;
	}
	return str;
    }

    /**
     * 在字符串右边添加addStr字符串达到固定长度
     * @param str	输入的字符串
     * @param addStr	添加字符
     * @param length	固定长度
     * @return
     */
    public static String addStringRight(String str, String addStr, int length) {
	StringBuilder builder = new StringBuilder(str);
	while (builder.length() < length) {
	    builder.append(addStr);
	}
	return builder.toString();
    }

    /**
     * 在字符串右边补齐0直到长度等于固定长度
     * @param str	输入的字符串
     * @param length	固定长度
     * @return
     */
    public static String addZeroRight(String str, int length) {
	return addStringRight(str, "0", length);
    }

    /**
     * 计算字符串中字符的个数
     * @param str	输入的字符串
     * @param sub	单个字符
     * @return
     */
    public static int charCount(String str, char sub) {
	int charCount = 0;
	int fromIndex = 0;

	while ((fromIndex = str.indexOf(sub, fromIndex)) != -1) {
	    fromIndex++;
	    charCount++;
	}
	return charCount;
    }

    /**
     * 格式化浮点型数字成字符串, 保留两位小数位.
     * @param number	浮点数字
     * @return 格式化之后的字符串
     */
    public static String formatDecimal(double number) {
	NumberFormat format = NumberFormat.getInstance();

	format.setMaximumIntegerDigits(Integer.MAX_VALUE);
	format.setMaximumFractionDigits(2);
	format.setMinimumFractionDigits(2);

	return format.format(number);
    }

    /**
     * 格式化浮点类型数据.
     * @param number	浮点数据
     * @param minFractionDigits	最小保留小数位
     * @param maxFractionDigits	最大保留小数位
     * @return 将浮点数据格式化后的字符串
     */
    public static String formatDecimal(double number, int minFractionDigits,
	    int maxFractionDigits) {
	NumberFormat format = NumberFormat.getInstance();
	format.setMinimumFractionDigits(minFractionDigits);
	format.setMaximumFractionDigits(minFractionDigits);

	return format.format(number);
    }
    
    /**
     * 把16进制表达的字符串转换为整数
     * @param hexString 输入的16进制的字符串
     * @return
     */
    public static int hexString2Int(String hexString) {
	return Integer.valueOf(hexString, 16).intValue();
    }
    
    /**
     * 获得成对出现的第一个关键字对应的关键字的位置。
     * 
     * @param str
     * @param keyword
     *            关键字，例如：select
     * @param oppositeKeyword
     *            对应的关键字，例如：from
     * @return 第一个关键字对应的关键字的位置
     */
    public static int getFirstPairIndex(String str, String keyword, String oppositeKeyword) {
        ArrayList<PairKeyword> keywordArray = new ArrayList<PairKeyword>();
        int index = -1;
        while ((index = str.indexOf(keyword, index)) != -1) {
            keywordArray.add(new PairKeyword(keyword, index));
            index += keyword.length();
        }

        index = -1;
        while ((index = str.indexOf(oppositeKeyword, index)) != -1) {
            keywordArray.add(new PairKeyword(oppositeKeyword, index));
            index += oppositeKeyword.length();
        }

        if (keywordArray.size() < 2) {
            return -1;
        }

        Collections.sort(keywordArray, new PairKeywordComparator());

        PairKeyword firstKeyword = keywordArray.get(0);
        if (!firstKeyword.getName().equals(keyword)) {
            return -1;
        }

        while (keywordArray.size() > 2) {
            boolean hasOpposite = false;
            for (int i = 2; i < keywordArray.size(); i++) {
                PairKeyword keyword0 = keywordArray.get(i - 1);
                PairKeyword keyword1 = keywordArray.get(i);
                if (keyword0.getName().equals(keyword) && keyword1.getName().equals(oppositeKeyword)) {
                    keywordArray.remove(i);
                    keywordArray.remove(i - 1);
                    hasOpposite = true;
                    break;
                }
            }
            if (!hasOpposite) {
                return -1;
            }
        }

        if (keywordArray.size() != 2) {
            return -1;
        }

        PairKeyword lastKeyword = keywordArray.get(1);
        if (!lastKeyword.getName().equals(oppositeKeyword)) {
            return -1;
        }

        return lastKeyword.getIndex();
    }
}
