package org.msh.etbm.services.cases;

import org.springframework.context.ApplicationEvent;

import java.util.UUID;

/**
 * Event type created and raised when a case action is executed
 * Created by Mauricio on 07/10/2016.
 */
public class CaseActionEvent extends ApplicationEvent {

    private CaseActionResponse response;

    public CaseActionEvent(Object source, CaseActionResponse response) {
        super(source);
        this.response = response;
    }

    public CaseActionEvent(Object source, UUID caseId, String caseDisplayString) {
        super(source);
        this.response = new CaseActionResponse();
        this.response.setCaseId(caseId);
        this.response.setCaseDisplayString(caseDisplayString);
    }

    public CaseActionResponse getResponse() {
        return response;
    }

    public void setResponse(CaseActionResponse response) {
        this.response = response;
    }
}
