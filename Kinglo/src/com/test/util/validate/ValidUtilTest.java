package com.test.util.validate;

import org.junit.Test;

import com.common.util.validator.ValidUtil;
import com.itextpdf.text.log.SysoLogger;


public class ValidUtilTest {
    
    @Test
    public void testExcel(){
	System.out.println(ValidUtil.isExcel(".xlt"));
    }
    
    @Test
    public void testEmail2(){
	System.out.println(ValidUtil.isEmail("1@.com"));
    }
    
    @Test
    public void testisIdCard(){
	System.out.println(ValidUtil.isIdCard("43052319901322007X"));
    }
    
    @Test
    public void testisChinese(){
	System.out.println(ValidUtil.isSimpleChinese("你妹的繁體"));
    }
}
