package org.msh.etbm.commons.entities;

import org.springframework.context.ApplicationEvent;

/**
 * Event type created and raised when Creating, updating or deleting an entity
 * Created by Mauricio on 05/10/2016.
 */
public class EntityServiceEvent extends ApplicationEvent {

    private ServiceResult result;

    public EntityServiceEvent(Object source, ServiceResult result) {
        super(source);
        this.result = result;
    }

    public ServiceResult getResult() {
        return result;
    }

    public void setResult(ServiceResult result) {
        this.result = result;
    }
}
