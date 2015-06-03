package org.msh.etbm.entities.enums;

public enum MedicineCategory {
	INJECTABLE,
	ORAL;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
