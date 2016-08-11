package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum ExtraOutcomeInfo implements MessageKey {
    CULTURE_SMEAR,
    CULTURE,
    CLINICAL_EXAM,
    TB,
    OTHER_CAUSES,
    TRANSFER_CATIV;

    @Override
    public String getMessageKey() {
        return "uk_UA." + getClass().getSimpleName().concat("." + name());
    }

}
