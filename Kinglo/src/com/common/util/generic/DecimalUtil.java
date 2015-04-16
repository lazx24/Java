package com.common.util.generic;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * 数字精度类
 * 类的名称:DecialUtil.java
 * 类的描述:
 * 创建人:邹建华
 * 创建时间:2014-6-14
 *
 */
public class DecimalUtil {
	/**
	 * 精度保留两位(会四舍五入)
	 * @param b
	 * @return Double
	 */
	public static Double decimalTwo(double b){
		return decimal(b,2);
	}
	
	/**
	 * 自定义精度位数
	 * @param b
	 * @param scale 精度位数
	 * @return Double
	 */
	public static Double decimal(double b,int scale){
		String format="%1$."+scale+"f";
		DecimalFormat decimal=new DecimalFormat(String.format(format,0.0));
		return new Double(decimal.format(b));
	}
	
	/**
	 * String转换为自定义精度的String
	 * @param strNum 
	 * @param format ("0.00")
	 * @return String 
	 */
	public static String numberFormat(String strNum,String format){
		DecimalFormat decimalFormat=new DecimalFormat(format);
		return decimalFormat.format(createBigDecimal(strNum).doubleValue());
	}
	
	/**
	 * String转换为自定义精度的bigDecimal
	 * @param strNum
	 * @param scale
	 * @param mode
	 * @return
	 */
	public static BigDecimal bigDecimalFormat(String strNum,int scale,int mode){
		BigDecimal bigDecimal = new BigDecimal(strNum);
		BigDecimal newBigDecimal=bigDecimal.setScale(scale, mode);
		return newBigDecimal;
	}
	
	/**
	 * 取bigDecimal的绝对值
	 * @param strNum
	 * @param scale
	 * @param mode
	 * @return
	 */
	public static BigDecimal abs(String strNum){
		return new BigDecimal(strNum).abs();
	}
	
	/**
	 * 取bigDecimal的反值
	 * @param strNum
	 * @return
	 */
	public static BigDecimal negate(String strNum){
		BigDecimal bigDecimal=new BigDecimal(strNum);
		return bigDecimal.negate();
	}
	
	/**
	 * 将该值的小数点向左移动 n 位
	 * @param n
	 * @return
	 */
	public static BigDecimal movePointLeft(String strNum,int n){
		return new BigDecimal(strNum).movePointLeft(n);
	}
	
	/**
	 * 将该值的小数点向右移动 n 位
	 * @param strNum
	 * @param n
	 * @return
	 */
	public static BigDecimal movePointRight(String strNum,int n){
		return new BigDecimal(strNum).movePointRight(n);
	}
	
	/*******************************精确计算*************************/
	//默认除法运算精度
	private static final int DEFAULT_DIV_SCALE = 10;
	
	public static BigDecimal createBigDecimal(double v) {
        return new BigDecimal(v);
    }
	
	public static BigDecimal createBigDecimal(String strVal) {
		if(strVal==null || strVal.equals("")){
			return new BigDecimal(0);
		}
        return new BigDecimal(strVal);
    }
	
	public static BigDecimal createBigDecimal(float f) {
        return new BigDecimal(f);
    }
	
	/**
	 * 提供精确的加法运算
	 * @param v1
	 * @param v2
	 * @return 
	 */
	public static double add(double v1, double v2) {
        BigDecimal b1 = createBigDecimal(v1);
        BigDecimal b2 = createBigDecimal(v2);
        return b1.add(b2).doubleValue();
    }
	
	/**
	 * 提供精确的减法运算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double subtract(double v1, double v2) {
        BigDecimal b1 = createBigDecimal(v1);
        BigDecimal b2 = createBigDecimal(v2);
        return b1.subtract(b2).doubleValue();
    }
	
	/**
	 * 提供精确的乘法运算
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double multiply(double v1, double v2) {
        BigDecimal b1 = createBigDecimal(v1);
        BigDecimal b2 = createBigDecimal(v2);

        return b1.multiply(b2).doubleValue();
    }
	
	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到小数点以后10位，以后的数字四舍五入
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double divide(double v1, double v2) {
        return divide(v1, v2, DEFAULT_DIV_SCALE);
    }
	
	/**
	 * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入
	 * @param v1
	 * @param v2
	 * @param scale
	 * @return
	 */
	public static double divide(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = createBigDecimal(v1);
        BigDecimal b2 = createBigDecimal(v2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
	
	/**
	 * 判断 double 值是否非法，值为 Infinite 或者 NaN 即表示非法
	 * @param v
	 * @return
	 */
	public static boolean isInvalidDouble(double v) {
        return Double.isInfinite(v) || Double.isNaN(v);
    }
	
	/**
	 * 提供精确的小数位四舍五入处理  如果 v 是非法的，则原样返回
	 * @param v
	 * @param scale
	 * @return
	 */
	public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (isInvalidDouble(v)) {
            return v;
        }
        BigDecimal b = createBigDecimal(v);
        return b.divide(BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
	/*******************************精确计算*************************/
	
	/*******************************随机数生成***********************/
	
	//没有添加 I、O的原因是避免和数字 1、0 混淆
	private static final String ALPHA_NUMERIC = "ABCDEFGHJKLMNPQRSTUVWXYZ123456789";
	
	/**
	 * 产生一个固定范围（min-max 之间）的随机正整数(包括min、max)
	 */
	public static int getRangeRandom(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
	
	/**
	 * 生成小于max的随机数
	 * @param max
	 * @return
	 */
	public static int getRangeRandom(int max) {
		return (int) (Math.random() * (double) max);
	}
	
	/**
	 * 获取一个指定范围的随机数字、根据时间断的不同、随机数字的范围也不同
	 * @param max
	 * @param min
	 * @return
	 */
	public static Integer getRandomNum(int max, int min) {
		Random random = new Random();
		int result = random.nextInt(max) % (max - min + 1) + min;
		return result;
	}
	
	/**
	 * 产生固定长度的随机字母数字串、 其中字母为大写方式
	 * @param length
	 * @return
	 */
	public static String getRandomLetter(int length) {
        char[] randomBytes = new char[length];
        for (int i = 0; i < length; i++) {
            randomBytes[i] = ALPHA_NUMERIC.charAt(getRangeRandom(0, ALPHA_NUMERIC.length() - 1));
        }
        return new String(randomBytes);
    }
	
	/**
	 * 产生固定长度的随机字母数字串、其中字母为小写方式
	 * @param length
	 * @return
	 */
	public static String getRandomLetterLowerCase(int length) {
        return getRandomLetter(length).toLowerCase();
    }
	
	/**
	 * 生成有前缀的随机数 
	 * @param prefix
	 * @return
	 */
	public static String getRandomNum(String prefix,int length){
		StringBuilder builder=new StringBuilder();
		builder.append(prefix).append(getRandomNum(length));
		return builder.toString();
	}
	
	/**
	 * 生成随机长度的字符串
	 * @param length
	 * @return
	 */
	public static String getRandomNum(int length){
		Random random=new Random();
		StringBuffer buffer=new StringBuffer();
		String len=prefixZero(1,length);
		int strLen=len.length();
		int  num=strLen%10==0?strLen/10:(strLen/10)+1;
		int mode=strLen%10;
		if(length<10){
			int randomNum=random.nextInt(Integer.parseInt(prefixZero(mode,mode)));
			int baseNum=Integer.parseInt(prefixZero(1,mode));
			buffer.append(randomNum+baseNum);
		}else{
			for (int i = 1; i <= num; i++) {
				if(i<num){
					buffer.append(random.nextInt(1000000000)+1000000000);
				}else if(i==num && mode!=0){
					int randomNum=random.nextInt(Integer.parseInt(prefixZero(mode,mode)));
					int baseNum=Integer.parseInt(prefixZero(1,mode));
					buffer.append(randomNum+baseNum);
				}else if(i==num && mode==0){
					buffer.append(random.nextInt(1000000000)+1000000000);
				}
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 例如 suffix=12 length=25 那么就会生成总长度为25位  中间补0 后缀是12
	 * 中间位置补0
	 */
	public static String suffixZero(int suffix,int length){
		return String.format("%0$0"+length+"d",suffix);
	}
	
	/**
	 * 例如prefix=1 length=2 那么就会生成10
	 * @param length
	 * @param prefix
	 * @return
	 */
	public static String prefixZero(int prefix,int length){
		return new StringBuilder().append(prefix).append(String.format("%0$0"+(length-new Integer(prefix).toString().length())+"d",0)).toString(); 
	}
	
	/*******************************随机数生成***********************/
}
