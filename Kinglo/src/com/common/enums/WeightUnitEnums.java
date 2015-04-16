package com.common.enums;

import java.io.Serializable;

public enum WeightUnitEnums implements Serializable {
	KG("KG"), TON("TON"), LBS("LBS");

	String value;

	private WeightUnitEnums(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}
