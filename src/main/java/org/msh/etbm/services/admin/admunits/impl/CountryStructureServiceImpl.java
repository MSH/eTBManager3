package org.msh.etbm.services.admin.admunits.impl;

import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.services.admin.admunits.CountryStructureData;
import org.msh.etbm.services.admin.admunits.CountryStructureQueryParams;
import org.msh.etbm.services.admin.admunits.CountryStructureService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;


/**
 * Interface of a service to handle CRUD operations in a country structure
 * Created by rmemoria on 24/10/15.
 */
@Service
public class CountryStructureServiceImpl extends EntityServiceImpl<CountryStructure, CountryStructureQueryParams>
    implements CountryStructureService {

    @Override
    protected void buildQuery(QueryBuilder<CountryStructure> builder, CountryStructureQueryParams queryParams) {
        builder.addDefaultProfile("default", CountryStructureData.class);

        builder.addDefaultOrderByMap(CountryStructureQueryParams.ORDERBY_LEVEL, "level, name");
        builder.addOrderByMap(CountryStructureQueryParams.ORDERBY_NAME, "name");
        builder.addOrderByMap(CountryStructureQueryParams.ORDERBY_LEVEL_DESC, "level desc, name desc");

        // filter by the level
        if (queryParams.getLevel() != null) {
            builder.addRestriction("level = :level");
            builder.setParameter("level", queryParams.getLevel());
        }

        // filter by the name
        if (queryParams.getName() != null) {
            builder.addRestriction("name = :name");
            builder.setParameter("name", queryParams.getName());
        }
    }

    @Override
    protected void prepareToSave(CountryStructure entity, BindingResult bindingResult) throws EntityValidationException {
        super.prepareToSave(entity, bindingResult);

        // there are error messages ?
        if (bindingResult.hasErrors()) {
            return;
        }

        if (!checkUnique(entity, "name")) {
            bindingResult.rejectValue("name", "NotUnique");
        }
    }

}
