package com.common.util.generic;

import com.common.util.bean.BeanUtil;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberUtil {
    private static final int DEF_DIV_SCALE = 10;
    private static NumberFormat numberFormatter = NumberFormat
	    .getNumberInstance();
    
    /**
     * 求平均值
     * @param inputData BigDecimal数组
     * @return
     */
    public static BigDecimal avg(BigDecimal[] inputData) {
	return avg(inputData, 10);
    }
    
    /**
     * 求平均值 
     * @param inputData BigDecimal数组
     * @param scale	保留几位小数
     * @return
     */
    public static BigDecimal avg(BigDecimal[] inputData, int scale) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return BigDecimal.ZERO;
	}
	long len = inputData.length;
	BigDecimal result = sum(inputData).divide(BigDecimal.valueOf(len),
		scale, 4);

	return result;
    }
    
    /**
     * 求平均值
     * @param inputData double数组
     * @return
     */
    public static Double avg(double[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return null;
	}
	int len = inputData.length;
	Double result = Double.valueOf(sum(inputData).doubleValue() / len);

	return result;
    }
    
    /**
     * 求平均值
     * @param inputData Double数组
     * @return
     */
    public static Double avg(Double[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return null;
	}
	int len = inputData.length;
	Double result = Double.valueOf(sum(inputData).doubleValue() / len);

	return result;
    }
    
    /**
     * long转换为字符串
     * @param number       long值
     * @param numberDigit  保留几位小数
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
     * Number转换
     * @param value      Object对象
     * @param dataFormat 字符串转换格式
     * @return
     */
    public static Object formatNumber(Object value, String dataFormat) {
	if (value == null) {
	    return null;
	}
	if ((value instanceof BigDecimal)) {
	    return new BigDecimal(String.format(dataFormat,
		    new Object[] { value }));
	}
	if (((value instanceof Double))
		|| (Double.TYPE.equals(value.getClass()))) {
	    return new Double(String.format(dataFormat, new Object[] { value }));
	}
	if (((value instanceof Float)) || (Float.TYPE.equals(value.getClass()))) {
	    return new Float(String.format(dataFormat, new Object[] { value }));
	}
	if ((value instanceof String)) {
	    try {
		BigDecimal v = new BigDecimal(value.toString());
		DecimalFormat df = new DecimalFormat(dataFormat);
		return df.format(v);
	    } catch (Exception localException) {
	    }
	}
	return value;
    }
    
    /**
     * 该字符串是否是Integer
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
	Pattern pattern = Pattern.compile("^[+-]?\\d+$");
	Matcher isNum = pattern.matcher(str);
	if (!isNum.matches()) {
	    return false;
	}
	return true;
    }
    
    /**
     * 是否是Number值
     * @param str 字符串
     * @return
     */
    public static boolean isNumberValue(String str) {
	Pattern pattern = Pattern.compile("^([+-]?\\d+)(\\.)?(\\d+)?$");
	Matcher isNum = pattern.matcher(str);
	if (!isNum.matches()) {
	    return false;
	}
	return true;
    }
    
    /**
     * 是否是Number
     * @param str 字符串
     * @return
     */
    public static boolean isNumber(String str) {
	Pattern pattern = Pattern.compile("[+-]?[0-9.]+$");
	Matcher isNum = pattern.matcher(str);
	if (!isNum.matches()) {
	    return false;
	}
	return true;
    }
    
    /**
     * 是否是Number或者百分比值
     * @param str
     * @return
     */
    public static boolean isNumberOrPercentValue(String str) {
	Pattern pattern = Pattern.compile("^([+-]?\\d+)(\\.)?(\\d+)?(%)?$");
	Matcher isNum = pattern.matcher(str);
	if (!isNum.matches()) {
	    return false;
	}
	return true;
    }
    
    /**
     * 是否是Number或者百分比
     * @param str
     * @return
     */
    public static boolean isNumberOrPercent(String str) {
	Pattern pattern = Pattern.compile("[+-]?[0-9.%]+$");
	Matcher isNum = pattern.matcher(str);
	if (!isNum.matches()) {
	    return false;
	}
	return true;
    }
    
    /**
     * 是否是Number或者正数的百分比值
     * @param str
     * @return
     */
    public static boolean isNumberOrPercentPositiveValue(String str) {
	Pattern pattern = Pattern.compile("^((+)?\\d+)(\\.)?(\\d+)?(%)?$");
	Matcher isNum = pattern.matcher(str);
	if (!isNum.matches()) {
	    return false;
	}
	return true;
    }
    
    /**
     * 是否是Number或者正数的百分比
     * @param str
     * @return
     */
    public static boolean isNumberOrPercentPositive(String str) {
	Pattern pattern = Pattern.compile("(+)?[0-9.%]+$");
	Matcher isNum = pattern.matcher(str);
	if (!isNum.matches()) {
	    return false;
	}
	return true;
    }
    
    /**
     * 获取最大的值
     * @param inputData BigDecimal数组
     * @return
     */
    public static BigDecimal max(BigDecimal[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return null;
	}
	int len = inputData.length;
	BigDecimal max = inputData[0];
	for (int i = 0; i < len; i++) {
	    if (max.compareTo(inputData[i]) == -1) {
		max = inputData[i];
	    }
	}
	return max;
    }

    /**
     * 获取最大的值
     * @param inputData double数组
     * @return
     */
    public static Double max(double[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return null;
	}
	int len = inputData.length;
	Double max = Double.valueOf(inputData[0]);
	for (int i = 0; i < len; i++) {
	    if (max.doubleValue() < inputData[i]) {
		max = Double.valueOf(inputData[i]);
	    }
	}
	return max;
    }
    
    /**
     * 获取最大的值
     * @param inputData Double数组
     * @return
     */
    public static Double max(Double[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return null;
	}
	int len = inputData.length;
	Double max = inputData[0];
	for (int i = 0; i < len; i++) {
	    if (max.doubleValue() < inputData[i].doubleValue()) {
		max = inputData[i];
	    }
	}
	return max;
    }
    
    /**
     * 获取最小的值
     * @param inputData BigDecimal数组
     * @return
     */
    public static BigDecimal min(BigDecimal[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return null;
	}
	int len = inputData.length;
	BigDecimal min = inputData[0];
	for (int i = 0; i < len; i++) {
	    if (min.compareTo(inputData[i]) == 1) {
		min = inputData[i];
	    }
	}
	return min;
    }
    
    /**
     * 获取最小的值
     * @param inputData double数组
     * @return
     */
    public static Double min(double[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return null;
	}
	int len = inputData.length;
	Double min = Double.valueOf(inputData[0]);
	for (int i = 0; i < len; i++) {
	    if (min.doubleValue() > inputData[i]) {
		min = Double.valueOf(inputData[i]);
	    }
	}
	return min;
    }
    
    /**
     * 获取最小的值
     * @param inputData Double数组
     * @return
     */
    public static Double min(Double[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return null;
	}
	int len = inputData.length;
	Double min = inputData[0];
	for (int i = 0; i < len; i++) {
	    if (min.doubleValue() > inputData[i].doubleValue()) {
		min = inputData[i];
	    }
	}
	return min;
    }
    
    /**
     * 求数组中数字的倍数之和
     * @param inputData
     * @return
     */
    public static Double squareSum(double[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return null;
	}
	int len = inputData.length;
	Double sqrsum = Double.valueOf(0.0D);
	for (int i = 0; i < len; i++) {
	    sqrsum = Double.valueOf(sqrsum.doubleValue() + inputData[i]
		    * inputData[i]);
	}
	return sqrsum;
    }

    public static Double standardDiviation(double[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return null;
	}
	Double result = Double.valueOf(Math.sqrt(Math.abs(variance(inputData)
		.doubleValue())));

	return result;
    }
    
    /**
     * 求和
     * @param inputData BigDecimal数组
     * @return
     */
    public static BigDecimal sum(BigDecimal[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return BigDecimal.ZERO;
	}
	int len = inputData.length;
	BigDecimal sum = BigDecimal.valueOf(0L);
	for (int i = 0; i < len; i++) {
	    if (inputData[i] != null) {
		sum = sum.add(inputData[i]);
	    }
	}
	return sum;
    }
    
    /**
     * 求和
     * @param inputData double数组
     * @return
     */
    public static Double sum(double[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return Double.valueOf(0.0D);
	}
	int len = inputData.length;
	Double sum = Double.valueOf(0.0D);
	for (int i = 0; i < len; i++) {
	    sum = Double.valueOf(sum.doubleValue() + inputData[i]);
	}
	return sum;
    }
    
    /**
     * 求和
     * @param inputData Double数组
     * @return
     */
    public static Double sum(Double[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return Double.valueOf(0.0D);
	}
	int len = inputData.length;
	Double sum = Double.valueOf(0.0D);
	for (int i = 0; i < len; i++) {
	    sum = Double
		    .valueOf(sum.doubleValue() + inputData[i].doubleValue());
	}
	return sum;
    }
    
    /**
     * 求方差
     * @param inputData
     * @return
     */
    public static Double variance(double[] inputData) {
	if ((inputData == null) || (inputData.length == 0)) {
	    return null;
	}
	int count = inputData.length;
	Double sqrsum = squareSum(inputData);
	Double average = avg(inputData);
	Double result = Double.valueOf((sqrsum.doubleValue() - count
		* average.doubleValue() * average.doubleValue())
		/ count);

	return result;
    }
    
    /**
     * 获取List列表之和
     * @param list	List列表
     * @param property  属性名
     * @return
     */
    public static BigDecimal sumListProperty(Collection<?> list, String property) {
	if (list == null) {
	    list = new ArrayList();
	}
	BigDecimal[] obs = new BigDecimal[list.size()];
	int index = 0;
	for (Object o : list) {
	    if (o != null) {
		try {
		    BigDecimal data = (BigDecimal) BeanUtil.getProperty(o,
			    property);
		    obs[(index++)] = data;
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		} catch (InvocationTargetException e) {
		    e.printStackTrace();
		} catch (NoSuchMethodException e) {
		    e.printStackTrace();
		}
	    }
	}
	return sum(obs);
    }
    
    /**
     * 获取List列表中最小值
     * @param list	List列表
     * @param property  属性名
     * @return
     */
    public static BigDecimal minListProperty(Collection<?> list, String property) {
	if (list == null) {
	    list = new ArrayList();
	}
	BigDecimal[] obs = new BigDecimal[list.size()];
	int index = 0;
	for (Object o : list) {
	    if (o != null) {
		try {
		    BigDecimal data = (BigDecimal) BeanUtil.getProperty(o,
			    property);
		    obs[(index++)] = data;
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		} catch (InvocationTargetException e) {
		    e.printStackTrace();
		} catch (NoSuchMethodException e) {
		    e.printStackTrace();
		}
	    }
	}
	return min(obs);
    }
    
    /**
     * 获取list列表最大值
     * @param list	List列表
     * @param property  属性名
     * @return
     */
    public static BigDecimal maxListProperty(Collection<?> list, String property) {
	if (list == null) {
	    list = new ArrayList();
	}
	BigDecimal[] obs = new BigDecimal[list.size()];
	int index = 0;
	for (Object o : list) {
	    if (o != null) {
		try {
		    BigDecimal data = (BigDecimal) BeanUtil.getProperty(o,
			    property);
		    obs[(index++)] = data;
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		} catch (InvocationTargetException e) {
		    e.printStackTrace();
		} catch (NoSuchMethodException e) {
		    e.printStackTrace();
		}
	    }
	}
	return min(obs);
    }
}
