package org.msh.etbm.services.admin.sources;


import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.db.entities.Source;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 * Service component to handle CRUD operations in Medicine Sources
 *
 * Created by rmemoria on 11/11/15.
 */
@Service
public class SourceServiceImpl extends EntityServiceImpl<Source, SourceQueryParams>
    implements SourceService {

    @Override
    protected void buildQuery(QueryBuilder<Source> builder, SourceQueryParams queryParams) {
        builder.addDefaultOrderByMap(SourceQueryParams.ORDERBY_NAME, "name");
        builder.addOrderByMap(SourceQueryParams.ORDERBY_SHORTNAME, "shortName");

        builder.addDefaultProfile("default", SourceData.class);

        // default to include just active items
        if (!queryParams.isIncludeDisabled()) {
            builder.addRestriction("active = true");
        }
    }


    @Override
    protected void prepareToSave(Source entity, BindingResult bindingResult) {
        super.prepareToSave(entity, bindingResult);

        if (bindingResult.hasErrors()) {
            return;
        }

        if (!checkUnique(entity, "name")) {
            bindingResult.rejectValue("name", ErrorMessages.NOT_UNIQUE);
        }

        if (!checkUnique(entity, "shortName")) {
            bindingResult.rejectValue("shortName", ErrorMessages.NOT_UNIQUE);
        }
    }
}
