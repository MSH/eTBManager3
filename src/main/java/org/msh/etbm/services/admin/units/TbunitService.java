package org.msh.etbm.services.admin.units;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.messages.MessageList;
import org.msh.etbm.db.entities.Tbunit;
import org.msh.etbm.db.repositories.UnitRepository;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 28/10/15.
 */
@Service
public class TbunitService extends EntityService<Tbunit, UnitRepository> {
    @Override
    protected void prepareToSave(Tbunit entity, MessageList msgs) throws EntityValidationException {
        super.prepareToSave(entity, msgs);
    }

}
