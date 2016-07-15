package org.msh.etbm.services.admin.admunits.impl;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.services.admin.admunits.CountryStructureData;
import org.msh.etbm.services.admin.admunits.CountryStructureQueryParams;
import org.msh.etbm.services.admin.admunits.CountryStructureService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;


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
    public String getCommandType() {
        return CommandTypes.ADMIN_COUNTRYSTRUCTURES;
    }

    @Override
    protected void beforeSave(CountryStructure cs, Errors errors) {
        if (!checkUnique(cs, "name", "level = " + cs.getLevel())) {
            errors.rejectValue("name", Messages.NOT_UNIQUE);
        }
    }
}
