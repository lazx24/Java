package com.coscon.shipsuite.common.util.generic;

import com.coscon.shipsuite.common.exception.ShipSuiteRuntimeException;
import com.coscon.shipsuite.common.util.bean.BeanUtil;
import java.math.BigDecimal;

public final class MoneyUtil {
    private static String[] HanDigiStr = { "零", "壹", "贰", "叁", "肆", "伍", "陆",
	    "柒", "捌", "玖" };
    private static String[] HanDiviStr = { "", "拾", "佰", "仟", "万", "拾", "佰",
	    "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟",
	    "万", "拾", "佰", "仟" };
    private static String[] HanUnitStr = { "负", "元", "角", "分", "整" };
    
    /**
     * MoneyVal转换为中文
     * @param moneyVal 
     * @return
     */
    public static String numberToChinese(BigDecimal moneyVal) {
	String SignStr = "";
	String TailStr = "";

	BigDecimal val = (BigDecimal) BeanUtil.deepClone(moneyVal);
	if (val.signum() == -1) {
	    val = val.abs();
	    SignStr = HanUnitStr[0];
	}
	if ((val.doubleValue() > 100000000000000.0D)
		|| (val.doubleValue() < -100000000000000.0D)) {
	    throw new ShipSuiteRuntimeException("validate.number.toolarge");
	}
	long temp = val.movePointRight(2).longValue();
	long integer = temp / 100L;
	long fraction = temp % 100L;
	int jiao = (int) fraction / 10;
	int fen = (int) fraction % 10;
	if ((jiao == 0) && (fen == 0)) {
	    TailStr = HanUnitStr[4];
	} else {
	    TailStr = HanDigiStr[jiao];
	    if (jiao != 0) {
		TailStr = TailStr + HanUnitStr[2];
	    }
	    if ((integer == 0L) && (jiao == 0)) {
		TailStr = "";
	    }
	    if (fen != 0) {
		TailStr = TailStr + HanDigiStr[fen] + HanUnitStr[3];
	    }
	}
	return SignStr + positiveIntegerToHanStr(String.valueOf(integer))
		+ HanUnitStr[1] + TailStr;
    }
    
    /**
     * MoneyVal转换为中文
     * @param moneyVal 
     * @return
     */
    public static String numberToChinese(double moneyVal) {
	return numberToChinese(BigDecimal.valueOf(moneyVal));
    }

    private static String positiveIntegerToHanStr(String NumStr) {
	String RMBStr = "";
	boolean lastzero = false;
	boolean hasvalue = false;

	int len = NumStr.length();
	if (len > 15) {
	    throw new ShipSuiteRuntimeException("validate.number.toolarge");
	}
	for (int i = len - 1; i >= 0; i--) {
	    if (NumStr.charAt(len - i - 1) != ' ') {
		int n = NumStr.charAt(len - i - 1) - '0';
		if ((n < 0) || (n > 9)) {
		    throw new ShipSuiteRuntimeException(
			    "validate.number.invalid");
		}
		if (n != 0) {
		    if (lastzero) {
			RMBStr = RMBStr + HanDigiStr[0];
		    }
		    if ((n != 1) || (i % 4 != 1) || (i != len - 1)) {
			RMBStr = RMBStr + HanDigiStr[n];
		    }
		    RMBStr = RMBStr + HanDiviStr[i];
		    hasvalue = true;
		} else if ((i % 8 == 0) || ((i % 8 == 4) && (hasvalue))) {
		    RMBStr = RMBStr + HanDiviStr[i];
		}
		if (i % 8 == 0) {
		    hasvalue = false;
		}
		lastzero = (n == 0) && (i % 4 != 0);
	    }
	}
	if (RMBStr.length() == 0) {
	    return HanDigiStr[0];
	}
	return RMBStr;
    }
}
