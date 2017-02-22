package org.msh.etbm.commons.entities;

import org.msh.etbm.commons.PersonNameUtils;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.CaseEntity;
import org.msh.etbm.db.entities.TbCase;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CaseEntityServiceImpl<E extends CaseEntity, Q extends EntityQueryParams> extends EntityServiceImpl<E, Q> {

    @Autowired
    PersonNameUtils personNameUtils;

    /**
     * Create the result to be returned by the create, update or delete operation of CaseEntity
     *
     * @param caseEntity the entity involved in the operation
     * @return instance of {@link ServiceResult}
     */
    @Override
    protected ServiceResult createResult(E caseEntity) {
        ServiceResult res = super.createResult(caseEntity);

        TbCase tbcase = caseEntity.getTbcase();

        res.setParentId(caseEntity.getTbcase().getId());
        res.setEntityName("(" + tbcase.getClassification() + ") " +
                personNameUtils.displayPersonName(tbcase.getPatient().getName()));

        return res;
    }
}
