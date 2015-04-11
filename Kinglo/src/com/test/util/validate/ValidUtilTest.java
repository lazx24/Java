package com.test.util.validate;

import org.junit.Test;

import com.coscon.shipsuite.common.util.validator.ValidUtil;


public class ValidUtilTest {
    
    @Test
    public void validateEnNumSymbol(){
	System.out.println(ValidUtil.validateEnNumSymbol("0889zdskdjf ]]"));
    }
    
    @Test
    public void validateDateSymbol(){
	System.out.println(ValidUtil.validateDateSymbol("2015-09-09"));
    }
}
