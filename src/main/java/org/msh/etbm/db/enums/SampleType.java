package org.msh.etbm.db.enums;

public enum SampleType {

	SPUTUM,
	OTHER,
	
	//Cambodia
	PUS,
	CSF,
	URINE,
	STOOL,
	TISSUE
	
	;
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
