package org.msh.etbm.entities.enums;

public enum ExtraOutcomeInfo {
	CULTURE_SMEAR,
	CULTURE,
	CLINICAL_EXAM,
	TB,
	OTHER_CAUSES,
	TRANSFER_CATIV;
	
	
	public String getKey() {
		return "uk_UA." + getClass().getSimpleName().concat("." + name());
	}

}
