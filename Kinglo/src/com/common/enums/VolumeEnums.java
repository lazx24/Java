package com.common.enums;

import java.io.Serializable;

public enum VolumeEnums implements Serializable {
    Cubic_Feet("CFT"), Cubic_Meters("CBM"), Liters("LIT"), Gallons("GAL"), Stere(
	    "Stere");

    private String value;

    public String getValue() {
	return this.value;
    }

    private VolumeEnums(String volume) {
	this.value = volume;
    }
}
