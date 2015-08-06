package org.msh.etbm.db.enums;


public enum OrderStatus {
	WAITAUTHORIZING,
	WAITSHIPMENT,
	SHIPPED,
	RECEIVED,
	CANCELLED,
	PREPARINGSHIPMENT;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
