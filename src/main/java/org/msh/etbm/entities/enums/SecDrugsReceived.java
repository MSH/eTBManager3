package org.msh.etbm.entities.enums;

public enum SecDrugsReceived {
	YES ("global.yes"),
	NO ("global.no"),
    UNKNOWN ("manag.ind.interim.unknown");

	private final String messageKey;

	SecDrugsReceived(String msg) {
		messageKey = msg;
	}
	
	public String getKey() {
		return messageKey;
	}
}
