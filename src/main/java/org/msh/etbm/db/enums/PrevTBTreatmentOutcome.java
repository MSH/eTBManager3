package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum PrevTBTreatmentOutcome implements MessageKey {

    CURED,
    COMPLETED,
    FAILURE,
    DEFAULTED,
    SCHEME_CHANGED,
    TRANSFERRED_OUT,
    SHIFT_CATIV,
    UNKNOWN,
    ONGOING,
    DIAGNOSTIC_CHANGED,
    NO_OUTCOME_YET,
    OTHER,
    /*Bangladesh*/DELAYED_CONVERTER;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

    /**
     * Convert from case outcome to previous TB case outcome
     *
     * @param state
     * @return
     */
    public static PrevTBTreatmentOutcome convertFromCaseState(CaseState state) {
        switch (state) {
            case CURED:
                return PrevTBTreatmentOutcome.CURED;
            case DEFAULTED:
                return DEFAULTED;
            case DIAGNOSTIC_CHANGED:
                return DIAGNOSTIC_CHANGED;
            case FAILED:
                return FAILURE;
            case MDR_CASE:
                return SHIFT_CATIV;
            case NOT_CONFIRMED:
                return UNKNOWN;
            case TREATMENT_COMPLETED:
                return COMPLETED;
            case TRANSFERRED_OUT:
                return TRANSFERRED_OUT;
            case OTHER:
                return OTHER;
            default:
                return PrevTBTreatmentOutcome.UNKNOWN;
        }
    }
}
