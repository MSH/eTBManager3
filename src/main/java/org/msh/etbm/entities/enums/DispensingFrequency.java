package org.msh.etbm.entities.enums;

public enum DispensingFrequency {
	MONTHLY,
	WEEKLY,
	DAILY;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
