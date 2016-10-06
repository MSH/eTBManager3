package org.msh.etbm.commons.entities;

import org.msh.etbm.db.enums.SearchableType;
import org.springframework.context.ApplicationEvent;

/**
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

    public String getEntityName() {
        if (result.getEntityClass() == null) {
            return null;
        }

        return result.getEntityClass().getSimpleName();
    }

    //TODO: [MSANTOS] find a better place to this for a better decoupling
    public boolean isSearchableEntity() {
        String entityName = getEntityName();

        if (entityName == null || entityName.isEmpty()) {
            return false;
        }

        for (SearchableType s : SearchableType.values()) {
            if (entityName.equals(s.getEntityClassName()) || entityName.equals(s.getParentClassName())) {
                return true;
            }
        }

        return false;
    }
}
