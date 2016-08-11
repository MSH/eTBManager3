package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

public enum MovementType implements MessageKey {
    DRUGRECEIVING,
    ORDERSHIPPING,
    ORDERRECEIVING,
    DISPENSING,
    ADJUSTMENT,
    TRANSFERIN,
    TRANSFEROUT,
    INITIALIZE;

    @Override
    public String getMessageKey() {
        return getClass().getSimpleName().concat("." + name());
    }

    /**
     * Returns the operation for the corresponding movement type
     *
     * @return
     */
    public int getOper() {
        switch (this) {
            case DRUGRECEIVING:
                return 1;
            case ORDERRECEIVING:
                return 1;
            case ORDERSHIPPING:
                return -1;
            case DISPENSING:
                return -1;
            case ADJUSTMENT:
                return 1;
            case TRANSFERIN:
                return 1;
            case TRANSFEROUT:
                return -1;
            case INITIALIZE:
                return 1;
        }

        return 0;
    }
}
