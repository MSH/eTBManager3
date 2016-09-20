package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.forms.FormInitResponse;

import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
public interface CaseService extends EntityService<CaseQueryParams> {
    FormInitResponse getReadOnlyForm(UUID id);
}
