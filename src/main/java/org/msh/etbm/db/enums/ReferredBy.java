package org.msh.etbm.db.enums;

public enum ReferredBy {
	VCT_CENTER,
	HIV_COMP_CARE_UNIT,
	STI_CLINIC,
	HOME_BASED_CARE,
	ANTENATAL_CLINIC,
	PRIVATE_SECTOR,
	PHARMACIST,
	SELF_REFERRAL,
	CONTACT_INVITATION,
	CHW, // Community Health Worker
	
	//Nigeria
	PUBLIC,
	PRIVATE_NON_PROFIT,
	PRIVATE_PROFIT,
	
	//Cambodia
	HEALTHCENTER,
	REFERRALHOSPITAL,
	AIDSPROGRAM,
	COMMUNITY,
	OTHER;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
