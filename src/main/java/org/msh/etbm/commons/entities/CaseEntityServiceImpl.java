package org.msh.etbm.commons.entities;

import org.msh.etbm.commons.Displayable;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.CaseEntity;
import org.msh.etbm.db.entities.TbCase;

public abstract class CaseEntityServiceImpl<E extends CaseEntity, Q extends EntityQueryParams> extends EntityServiceImpl<E, Q> {

    /**
     * Create the result to be returned by the create, update or delete operation of CaseEntity
     *
     * @param caseEntity the entity involved in the operation
     * @return instance of {@link ServiceResult}
     */
    @Override
    protected ServiceResult createResult(E caseEntity) {
        ServiceResult res = new ServiceResult();

        res.setId(caseEntity.getId());
        res.setEntityClass(getEntityClass());

        String cmdPath = getCommandType();
        res.setCommandType(CommandTypes.get(cmdPath));

        res.setEntityName(caseEntity.getTbcase().getDisplayString());
        res.setId(caseEntity.getTbcase().getId());
        res.setEntityClass(TbCase.class);

        return res;
    }

}
