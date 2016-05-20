package org.msh.etbm.db.enums;

public enum FollowUpType {
	MEDEXAM,
	MICROSCOPY,
	CULTURE,
	XPERT,
	DST,
	XRAY,
	HIV;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
