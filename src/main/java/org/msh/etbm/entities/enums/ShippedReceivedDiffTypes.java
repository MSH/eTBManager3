package org.msh.etbm.entities.enums;

public enum ShippedReceivedDiffTypes {
	NONE,
	SHIPPED_BT_RECEIVED,
	RECEIVED_BT_SHIPPED,
	BOTH;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
