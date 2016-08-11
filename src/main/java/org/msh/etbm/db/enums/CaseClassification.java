package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

/**
 * Classification of the cases
 *
 * @author Ricardo Memoria
 */
public enum CaseClassification implements MessageKey{
    TB,
    DRTB,
    NTM;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

    public String getKeySuspect() {
        return getClass().getSimpleName().concat("." + name() + ".suspect");
    }

    public String getKey2() {
        return name();
    }
}
