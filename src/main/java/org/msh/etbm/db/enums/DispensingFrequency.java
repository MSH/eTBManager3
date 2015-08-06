package org.msh.etbm.db.enums;

public enum DispensingFrequency {
	MONTHLY,
	WEEKLY,
	DAILY;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
