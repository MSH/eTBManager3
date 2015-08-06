package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum ClinicalEvolution implements MessageKey {

	FAVORABLE,
	UNCHANGED,
	UNFAVORABLE,
	//Brazil
	FAILED;
	
	public String getKey() {
		return "pt_BR." + getClass().getSimpleName().concat("." + name());
	}

	@Override
	public String getMessageKey() {
		return getKey();
	}
}
