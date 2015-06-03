package org.msh.etbm.entities.enums;

public enum Gender {
	MALE,
	FEMALE;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
