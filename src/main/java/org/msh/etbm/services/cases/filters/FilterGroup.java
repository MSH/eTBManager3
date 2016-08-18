package org.msh.etbm.services.cases.filters;

import org.msh.etbm.db.MessageKey;

/**
 * Created by rmemoria on 17/8/16.
 */
public enum FilterGroup implements MessageKey {

    DATA("cases.details.case"),
    EXAM_MICROSCOPY("cases.exammicroscopy"),
    EXAM_CULTURE("cases.examculture"),
    EXAM_DST("cases.examdst"),
    EXAM_XPERT("cases.examxpert"),
    EXAM_HIV("cases.examhiv"),
    TREATMENT("cases.details.treatment"),
    PREVIOUS_TREATMENT("cases.prevtreat"),
    OTHERS("form.otherfilters");

    private String messageKey;

    FilterGroup(String messageKey) {
        this.messageKey = messageKey;
    }

    @Override
    public String getMessageKey() {
        return messageKey;
    }

}
