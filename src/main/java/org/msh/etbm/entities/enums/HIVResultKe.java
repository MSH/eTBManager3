package org.msh.etbm.entities.enums;

public enum HIVResultKe {
	POSITIVE,
	NEGATIVE,
	ONGOING,
	DECLINED, 
	NO_PARTNER;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
