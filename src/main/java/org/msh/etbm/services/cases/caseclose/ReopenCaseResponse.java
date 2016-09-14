package org.msh.etbm.services.cases.caseclose;

import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.cases.CaseActionResponse;

import java.util.UUID;

/**
 * Created by Mauricio on 26/07/2016.
 */
public class ReopenCaseResponse extends CaseActionResponse {

    CaseState state;

    public ReopenCaseResponse(UUID tbcaseId, String tbcaseDisplayString, CaseState state) {
        super(tbcaseId, tbcaseDisplayString);
        this.state = state;
    }

    public CaseState getState() {
        return state;
    }

    public void setState(CaseState state) {
        this.state = state;
    }
}
