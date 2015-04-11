package com.coscon.shipsuite.common.enums;

import java.io.Serializable;
/**
 * 字母大小写
 * @author zou
 * 2015-1-29
 */
public enum CharacterCasingEnums implements Serializable {
    Normal(0), Lower(1), Upper(2);

    int value;

    private CharacterCasingEnums(int value) {
	this.value = value;
    }

    public int getValue() {
	return this.value;
    }

    public void setValue(int value) {
	this.value = value;
    }
}
