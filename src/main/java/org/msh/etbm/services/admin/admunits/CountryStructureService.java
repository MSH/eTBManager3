package org.msh.etbm.services.admin.admunits;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.messages.MessageList;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.db.repositories.CountryStructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


/**
 * Interface of a service to handle CRUD operations in a country structure
 * Created by rmemoria on 24/10/15.
 */
@Service
public class CountryStructureService extends EntityService<CountryStructure, CountryStructureRepository> {

    @Autowired
    MessageSource messageSource;

    @Override
    protected void prepareToSave(CountryStructure entity, MessageList msgs) {
        super.prepareToSave(entity, msgs);

        // there are error messages ?
        if (msgs.size() > 0) {
            return;
        }

        if (!isUniqueEntity(entity)) {
            msgs.addNotUnique("name");
        }
    }

    @Override
    protected boolean isUniqueEntity(CountryStructure cs) {
        CountryStructureRepository rep = getCrudRepository();
        List<CountryStructure> lst = rep.findByNameAndWorkspaceIdAndLevel(cs.getName(),
                getWorkspaceId(),
                cs.getLevel());

        UUID id = cs.getId();

        if (id == null && lst.size() > 0) {
            return false;
        }

        for (CountryStructure aux: lst) {
            if (!aux.getId().equals(id)) {
                return false;
            }
        }
        return true;
    }
}
