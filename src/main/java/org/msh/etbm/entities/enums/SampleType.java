package org.msh.etbm.entities.enums;

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
