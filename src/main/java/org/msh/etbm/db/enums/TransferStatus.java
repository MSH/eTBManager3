package org.msh.etbm.db.enums;

public enum TransferStatus {
	WAITING_RECEIVING,
	DONE,
	CANCELLED;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}

}
