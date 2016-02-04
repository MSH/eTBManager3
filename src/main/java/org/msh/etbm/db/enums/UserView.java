package org.msh.etbm.db.enums;

public enum UserView {
	COUNTRY,
	ADMINUNIT,
	TBUNIT,
    SELECTEDUNITS;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
	
}
