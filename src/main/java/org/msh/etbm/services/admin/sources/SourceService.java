package org.msh.etbm.services.admin.sources;

import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

/**
 * Service component to handle CRUD operations in Medicine Sources
 *
 * Created by rmemoria on 11/11/15.
 */
@Service
public class SourceService extends EntityService<Source> {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    /**
     * Return a list of sources by the given query
     * @param qry filters and constraints about what to return
     * @return result of the query
     */
    public QueryResult<SourceData> findMany(SourceQueryParams qry) {
        QueryBuilder<Source> builder = queryBuilderFactory.createQueryBuilder(Source.class);

        builder.addDefaultOrderByMap(SourceFormData.ORDERBY_NAME, "name");
        builder.addOrderByMap(SourceFormData.ORDERBY_SHORTNAME, "shortName");

        builder.addDefaultProfile("default", SourceData.class);

        builder.initialize(qry);

        // default to include just active items
        if (!qry.isIncludeDisabled()) {
            builder.addRestriction("active = true");
        }

        QueryResult<SourceData> res = builder.createQueryResult();
        return res;
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
