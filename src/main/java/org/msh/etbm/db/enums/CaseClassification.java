package org.msh.etbm.db.enums;

/**
 * Classification of the cases
 * @author Ricardo Memoria
 *
 */
public enum CaseClassification {
	TB,
	DRTB,
	NTM;


	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
	
	public String getKeySuspect() {
		return getClass().getSimpleName().concat("." + name() + ".suspect");
	}
	
	public String getKey2() {
		return name();
	}
}
