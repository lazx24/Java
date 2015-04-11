package com.coscon.shipsuite.common.util.validator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import com.coscon.shipsuite.common.util.generic.DateUtil;
import com.coscon.shipsuite.common.util.string.StringUtil;

public class ValidUtil {

    /**
     * 简体中文的正则表达式。
     */
    private static final String REGEX_SIMPLE_CHINESE = "^[\u4E00-\u9FA5]+$";

    /**
     * 字母数字的正则表达式。
     */
    private static final String REGEX_ALPHANUMERIC = "[a-zA-Z0-9]+";

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
    private static final String REGEX_EMAIL = ".+@.+\\.[a-z]+";

    /**
     * 电话号码的正则表达式。
     */
    private static final String REGEX_PHONE_NUMBER = "(([\\(（]\\d+[\\)）])?|(\\d+[-－]?)*)\\d+";

    // 加权因子
    private static final int[] weight = new int[] { 7, 9, 10, 5, 8, 4, 2, 1, 6,
	    3, 7, 9, 10, 5, 8, 4, 2, 1 };
    // 校验码
    private static final int[] checkDigit = new int[] { 1, 0, 'X', 9, 8, 7, 6,
	    5, 4, 3, 2 };

    /**
     * 手机号码
     * 
     * @param phoneNo
     * @return
     */
    public static boolean isMobileNo(String mobileNo) {
	try {
	    String regex = "^1[\\d]{10}$";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(mobileNo);
	    return matcher.matches();

	} catch (RuntimeException e) {
	    return false;
	}
    }

    /**
     * 邮箱
     */
    public static boolean isEmail2(String email) {
	return Pattern
		.compile(
			"^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[0-9a-zA-Z]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]{2}|net|NET|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|cc|CC)$")
		.matcher(email).matches();
    }

    /**
     * 验证邮政编码
     */
    public static boolean isPostalCode(String postalCode) {
	return Pattern.compile("^[1-9][0-9]{5}$").matcher(postalCode).matches();
    }

    /**
     * 验证EXCEL
     */
    public static boolean isExcel(String fileName) {
	return Pattern.compile("^.+\\.(xls)$").matcher(fileName).matches();
    }

    /**
     * 验证文件格式
     */
    public static boolean isFile(String fileName) {
	return Pattern
		.compile(
			"^.+\\.(?:jpg|gif|doc|docx|xls|xlsx|ppt|pdf|tif|bmp|rtf)$")
		.matcher(fileName).matches();
    }

    /**
     * 验证字符串是数字
     */
    public static boolean isNumber2(String str) {
	return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    /**
     * 判断是否是图片(根据后缀名不靠谱)
     * 
     * @param fileName
     * @return
     */
    public static boolean isImage(String fileName) {
	BufferedImage bi;
	try {
	    bi = ImageIO.read(new File(fileName));
	    if (bi == null) {
		System.out.println("此文件不是图片!");
		return false;
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    /**
     * 验证身份证格式是否正确
     * 
     * @param idCard
     * @return
     */
    public static boolean isIdCard(String idCard) {
	if (idCard.length() == 15) {
	    idCard = update2eighteen(idCard);
	}
	if (idCard.length() != 18) {
	    return false;
	}
	// 获取输入身份证上的最后一位，它是校验码
	String checkDigit = idCard.substring(17, 18);
	// 比较获取的校验码与本方法生成的校验码是否相等
	if (checkDigit.equals(getCheckDigit(idCard))) {
	    return true;
	}
	return false;
    }

    /**
     * 计算18位身份证的校验码
     * 
     * @param eighteenCardID
     *            18位身份证
     * @return
     */
    private static String getCheckDigit(String eighteenCardID) {
	int remaining = 0;
	if (eighteenCardID.length() == 18) {
	    eighteenCardID = eighteenCardID.substring(0, 17);
	}

	if (eighteenCardID.length() == 17) {
	    int sum = 0;
	    int[] a = new int[17];
	    // 先对前17位数字的权求和
	    for (int i = 0; i < 17; i++) {
		String k = eighteenCardID.substring(i, i + 1);
		a[i] = Integer.parseInt(k);
	    }
	    for (int i = 0; i < 17; i++) {
		sum = sum + weight[i] * a[i];
	    }
	    // 再与11取模
	    remaining = sum % 11;
	}
	return remaining == 2 ? "X" : String.valueOf(checkDigit[remaining]);
    }

    /**
     * 将15位身份证升级成18位身份证号码
     * 
     * @param fifteenCardID
     * @return
     */
    private static String update2eighteen(String fifteenCardID) {
	// 15位身份证上的生日中的年份没有19，要加上
	String eighteenCardID = fifteenCardID.substring(0, 6);
	eighteenCardID = eighteenCardID + "19";
	eighteenCardID = eighteenCardID + fifteenCardID.substring(6, 15);
	eighteenCardID = eighteenCardID + getCheckDigit(eighteenCardID);
	return eighteenCardID;
    }

    /**
     * 是否是质数
     * 
     * @param x
     * @return
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
     * 是否是中文
     * 
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
	Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
	return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为浮点数，包括double和float
     * 
     * @param str
     *            传入的字符串
     * @return 是浮点数返回true,否则返回false
     */
    public static boolean isDouble(String str) {
	Pattern pattern = Pattern.compile("^[-\\+]?\\d+\\.\\d+$");
	return pattern.matcher(str).matches();
    }

    /**
     * 功能描述：判断是否为整数
     * 
     * @param str
     *            传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
	Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
	return pattern.matcher(str).matches();
    }

    public static boolean isEmpty(Object object) {
	return object == null ? true : false;
    }

    /**
     * 判断字符串是否只包含字母和数字.
     * 
     * @param str
     *            字符串
     * @return 如果字符串只包含字母和数字, 则返回 <code>true</code>, 否则返回 <code>false</code>.
     */
    public static boolean isAlphanumeric(String str) {
	return isRegexMatch(str, REGEX_ALPHANUMERIC);
    }

    /**
     * 是否为中国移动手机号码。
     * 
     * @param str
     *            字符串
     * @return 如果是移动号码，返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isChinaMobile(String str) {
	return isRegexMatch(str, REGEX_CHINA_MOBILE);
    }

    /**
     * 是否为中国联通手机号码。
     * 
     * @param str
     *            字符串
     * @return 如果是联通号码，返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isChinaUnicom(String str) {
	return isRegexMatch(str, REGEX_CHINA_UNICOM);
    }

    /**
     * 判断是否为电信手机。
     * 
     * @param str
     *            字符串
     * @return 如果是电信号码，返回 <code>true</code>，否则返回 <code>false</code>。
     */
    public static boolean isChinaTelecom(String str) {
	return isRegexMatch(str, REGEX_CHINA_TELECOM);
    }

    /**
     * 判断是否为小灵通手机(Personal Access Phone System, PAS)。
     * 
     * @param str
     *            字符串
     * @return 如果是小灵通号码，返回 <code>true</code>，否则返回 <code>false</code>。
     * @deprecated 已经被 {@link #isChinaTelecom(String)} 取代
     */
    @Deprecated
    public static boolean isChinaPAS(String str) {
	return isChinaTelecom(str);
    }

    /**
     * 是否是合法的日期字符串
     * 
     * @param str
     *            日期字符串
     * @return 是true，否则false
     */
    public static boolean isDate(String str) {
	if (isEmpty(str) || str.length() > 10) {
	    return false;
	}

	String[] items = str.split("-");

	if (items.length != 3) {
	    return false;
	}

	if (!isNumber(items[0], 1900, 9999) || !isNumber(items[1], 1, 12)) {
	    return false;
	}

	int year = Integer.parseInt(items[0]);
	int month = Integer.parseInt(items[1]);

	return isNumber(items[2], 1, DateUtil.getMaxDayOfMonth(year, month - 1));
    }

    /**
     * 是否是合法的日期时间字符串
     * 
     * @param str
     *            日期时间字符串
     * @return 是true，否则false
     */
    public static boolean isDateTime(String str) {
	if (isEmpty(str) || str.length() > 20) {
	    return false;
	}

	String[] items = str.split(" ");

	if (items.length != 2) {
	    return false;
	}

	return isDate(items[0]) && isTime(items[1]);
    }

    /**
     * 判断字符串是否是合法的电子邮箱地址.
     * 
     * @param str
     *            字符串
     * @return 是true，否则false
     */
    public static boolean isEmail(String str) {
	return isRegexMatch(str, REGEX_EMAIL);
    }

    /**
     * 字符串是否为Empty，null和空格都算是Empty
     * 
     * @param str
     *            字符串
     * @return true/false
     */
    public static boolean isEmpty(String str) {
	return str == null || str.trim().length() == 0;
    }

    /**
     * 判断集合是否为空。
     * 
     * @param <T>
     *            集合泛型
     * @param collection
     *            集合对象
     * @return 当集合对象为 <code>null</code> 或者长度为零时返回 <code>true</code>，否则返回
     *         <code>false</code>。
     */
    public static <T> boolean isEmpty(Collection<T> collection) {
	return collection == null || collection.isEmpty();
    }

    /**
     * <p>
     * Validating for ID card number.
     * </p>
     * 
     * @param str
     *            string to be validated
     * @return If the str is valid ID card number return <code>true</code>,
     *         otherwise return <code>false</code>.
     */
    public static boolean isIdCardNumber(String str) {
	// 15位或18数字, 14数字加x(X)字符或17数字加x(X)字符才是合法的
	return isRegexMatch(str, REGEX_ID_CARD);
    }

    /**
     * 是否为手机号码, 包括移动, 联通, 小灵通等手机号码.
     * 
     * @param str
     *            字符串
     * @return 若是合法的手机号码返回 <code>true</code>, 否则返回 <code>false</code>.
     */
    public static boolean isMobile(String str) {
	return isChinaMobile(str) || isChinaUnicom(str) || isChinaPAS(str);
    }

    /**
     * 是否为数字的字符串。
     * 
     * @param str
     *            字符串
     * @return true/false
     */
    public static boolean isNumber(String str) {
	if (isEmpty(str)) {
	    return false;
	}

	for (int i = 0; i < str.length(); i++) {
	    if (str.charAt(i) > '9' || str.charAt(i) < '0') {
		return false;
	    }
	}
	return true;
    }

    /**
     * 是否是固定范围内的数字的字符串
     * 
     * @param str
     * @param min
     * @param max
     * @return true/false
     */
    public static boolean isNumber(String str, int min, int max) {
	if (!isNumber(str)) {
	    return false;
	}

	int number = Integer.parseInt(str);
	return number >= min && number <= max;
    }

    /**
     * 判断字符是否为整数或浮点数. <br>
     * 
     * @param str
     *            字符串
     * @return 若为整数或浮点数则返回 <code>true</code>, 否则返回 <code>false</code>
     */
    public static boolean isNumeric(String str) {
	return isRegexMatch(str, REGEX_NUMERIC);
    }

    /**
     * 判断字符是否为符合精度要求的整数或浮点数。
     * 
     * @param str
     *            字符串
     * @param fractionNum
     *            小数部分的最多允许的位数
     * @return 若为整数或浮点数则返回 <code>true</code>, 否则返回 <code>false</code>
     */
    public static boolean isNumeric(String str, int fractionNum) {
	if (isEmpty(str)) {
	    return false;
	}

	// 整数或浮点数
	String regex = "(\\+|-){0,1}(\\d+)([.]?)(\\d{0," + fractionNum + "})";
	return Pattern.matches(regex, str);
    }

    /**
     * <p>
     * Validating for phone number.
     * </p>
     * 
     * e.g. <li>78674585 --> valid</li> <li>6872-4585 --> valid</li> <li>
     * (6872)4585 --> valid</li> <li>0086-10-6872-4585 --> valid</li> <li>
     * 0086-(10)-6872-4585 --> invalid</li> <li>0086(10)68724585 --> invalid</li>
     * 
     * @param str
     *            string to be validated
     * @return If the str is valid phone number return <code>true</code>,
     *         otherwise return <code>false</code>.
     */
    public static boolean isPhoneNumber(String str) {
	// Regex for checking phone number
	return isRegexMatch(str, REGEX_PHONE_NUMBER);
    }

    /**
     * 判断是否是合法的邮编
     * 
     * @param str
     *            字符串
     * @return true/false
     */
    public static boolean isPostcode(String str) {
	if (isEmpty(str)) {
	    return false;
	}

	if (str.length() != 6 || !isNumber(str)) {
	    return false;
	}

	return true;
    }

    /**
     * 判断是否是固定长度范围内的字符串
     * 
     * @param str
     * @param minLength
     * @param maxLength
     * @return true/false
     */
    public static boolean isString(String str, int minLength, int maxLength) {
	if (str == null) {
	    return false;
	}

	if (minLength < 0) {
	    return str.length() <= maxLength;
	} else if (maxLength < 0) {
	    return str.length() >= minLength;
	} else {
	    return str.length() >= minLength && str.length() <= maxLength;
	}
    }

    /**
     * 判断是否是合法的时间字符串。
     * 
     * @param str
     *            字符串
     * @return true/false
     */
    public static boolean isTime(String str) {
	if (isEmpty(str) || str.length() > 8) {
	    return false;
	}

	String[] items = str.split(":");

	if (items.length != 2 && items.length != 3) {
	    return false;
	}

	for (int i = 0; i < items.length; i++) {
	    if (items[i].length() != 2 && items[i].length() != 1) {
		return false;
	    }
	}

	return !(!isNumber(items[0], 0, 23) || !isNumber(items[1], 0, 59) || (items.length == 3 && !isNumber(
		items[2], 0, 59)));
    }

    /**
     * 是否是简体中文字符串。
     * 
     * @param str
     *            字符串
     * @return true/false
     */
    public static boolean isSimpleChinese(String str) {
	return isRegexMatch(str, REGEX_SIMPLE_CHINESE);
    }

    /**
     * 判断字符串是否匹配了正则表达式。
     * 
     * @param str
     *            字符串
     * @param regex
     *            正则表达式
     * @return true/false
     */
    public static boolean isRegexMatch(String str, String regex) {
	return str != null && str.matches(regex);
    }

    public static boolean isUpperCase(Character c) {
	return Character.isUpperCase(c);
    }

    /**
     * 判断 double 值是否非法，值为 Infinite 或者 NaN 即表示非法
     * 
     * @param v
     * @return
     */
    public static boolean isInvalidDouble(double v) {
	return Double.isInfinite(v) || Double.isNaN(v);
    }
    
    /**
     * 判断是否是数字和字母以及各种特殊符号
     * @param value
     * @return
     */
    public static boolean validateEnNumSymbol(String value) {
	return matches(
		value,
		"^[A-Za-z0-9 `\\-=\\[\\]\\\\;',\\./~!@#\\$%\\^&\\*\\(\\)_\\+\\{\\}\\|:\\\"<>\\?\\r\\n]*$");
    }
    
    /**
     * 判断是否是数字以及各种特殊符号
     * @param value
     * @return
     */
    public static boolean validateNumSymbol(String value) {
	return matches(
		value,
		"^[0-9 `\\-=\\[\\]\\\\;',\\./~!@#\\$%\\^&\\*\\(\\)_\\+\\{\\}\\|:\\\"<>\\?\\r\\n]*$");
    }
    
    /**
     * 验证版本号
     * @param value
     * @return
     */
    public static boolean validateVersion(String value) {
	return matches(value, "[\\d\\.]+\\d$");
    }
    
    /**
     * 验证是否是日期
     * @param value
     * @return
     */
    public static boolean validateDateSymbol(String value) {
	if (StringUtil.isNullOrEmptyWithTrim(value)) {
	    return true;
	}
	return matches(value,
		"^[0-9 \\-\\[\\]\\\\/~\\(\\)_\\{\\}\\|:\\<> \\.年月日时分秒（）【】　]*$");
    }

    public static boolean matches(String value, String patternString) {
	Pattern pattern = null;
	Matcher matcher = null;
	pattern = Pattern.compile(patternString);
	matcher = pattern.matcher(value);

	return matcher.matches();
    }
    
    /**
     * 判断是否是数字和字母
     * @param value
     * @return
     */
    public static boolean validateEnNum(String value) {
	return matches(value, "^[A-Za-z0-9]*$");
    }
}
