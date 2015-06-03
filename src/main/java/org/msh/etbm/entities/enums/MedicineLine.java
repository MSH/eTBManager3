package org.msh.etbm.entities.enums;

public enum MedicineLine {
	FIRST_LINE,
	SECOND_LINE,
	OTHER;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
