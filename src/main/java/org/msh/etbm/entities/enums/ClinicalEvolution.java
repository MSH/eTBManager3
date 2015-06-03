package org.msh.etbm.entities.enums;

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
