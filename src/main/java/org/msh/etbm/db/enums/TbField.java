package org.msh.etbm.db.enums;


import org.msh.etbm.db.MessageKey;

/**
 * Field
 *
 * @author Ricardo
 */
public enum TbField implements MessageKey {

    TBDETECTION,
    DIAG_CONFIRMATION,
    POSITION,
    SIDEEFFECT,
    COMORBIDITY,
    PHYSICAL_EXAMS,
    DST_METHOD,
    CULTURE_METHOD,
    SMEAR_METHOD,
    SYMPTOMS,
    CONTACTTYPE,
    CONTACTCONDUCT,
    XRAYPRESENTATION,
    PULMONARY_TYPES,
    EXTRAPULMONARY_TYPES,
    // Brazilian custom variables
    SKINCOLOR, PREGNANCE_PERIOD, EDUCATIONAL_DEGREE, CONTAG_PLACE, SCHEMA_TYPES,
    RESISTANCE_TYPES, MICOBACTERIOSE, MOLECULARBIOLOGY_METHOD,
    // ukraine
    REGISTRATION_CATEGORY,
    // Bangladesh
    BIOPSY_METHOD,
    //Namibia
    ART_REGIMEN,
    // Azerbaijan
    MARITAL_STATUS, SEVERITY_MARKS,
    // Brazil
    XRAY_CONTACT,
    // Azerbaijan
    XRAY_LOCALIZATION,
    // Uzbekistan
    ANOTHERTB,
    //Nigeria
    RISK_GROUP,
    SUSPECT_TYPE,
    //Generic
    ADJUSTMENT,
    //Cambodia
    IDENTIFICATION,
    //Brazil
    TREATMENT_OUTCOME_ILTB,
    //Indonesia
    SUSPECT_CRITERIA,
    //Ukraine
    CAUSE_OF_CHANGE,
    MANUFACTURER,
    //Bangladesh
    MEDEXAM_DOTTYPE,
    MEDEXAM_REFTOTYPE,
    SOURCE_REFERRAL,
    OCCUPATION,
    REASON_XPERT_EXAM;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }
}
