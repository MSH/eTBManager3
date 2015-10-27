package org.msh.etbm.services.admin.admunits;

import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.db.repositories.CountryStructureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


/**
 * Interface of a service to handle CRUD operations in a country structure
 * Created by rmemoria on 24/10/15.
 */
@Service
public class CountryStructureService extends EntityService<CountryStructure, CountryStructureRepository> {
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
