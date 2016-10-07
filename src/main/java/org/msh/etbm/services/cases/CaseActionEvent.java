package org.msh.etbm.services.cases;

import org.springframework.context.ApplicationEvent;

/**
 * Event type created and raised when a case action is executed
 * Created by Mauricio on 07/10/2016.
 */
public class CaseActionEvent extends ApplicationEvent {

    private CaseActionResponse res;

    public CaseActionEvent(Object source, CaseActionResponse res) {
        super(source);
        this.res = res;
    }

    public CaseActionResponse getRes() {
        return res;
    }

    public void setRes(CaseActionResponse res) {
        this.res = res;
    }
}
