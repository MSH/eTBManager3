package org.msh.etbm.services.cases.tag;

import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.admin.tags.TagData;
import org.msh.etbm.services.admin.tags.TagService;
import org.msh.etbm.services.cases.cases.data.CaseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Service to generate a list about the cases of a given tag
 *
 * Created by Mauricio on 09/08/2016.
 */
@Service
public class TagCasesQueryService {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TagService tagService;

    public QueryResult getTagCases(TagCasesQueryParams qryParams) {
        QueryBuilder<TbCase> builder = queryBuilderFactory.createQueryBuilder(TbCase.class);

        builder.setHqlSelect("select c");
        builder.setEntityAlias("c");
        builder.addDefaultProfile("caseitem", CaseItem.class);

        builder.setHqlJoin("join c.tags t");
        builder.addRestriction("t.id = :tId", qryParams.getTagId());
        builder.addRestriction("c.ownerUnit.id = :unitId", qryParams.getUnitId());
        // filter by administrative unit
        if (qryParams.getAdminUnitId() != null) {
            AdministrativeUnit adminUnit = entityManager.find(AdministrativeUnit.class, qryParams.getAdminUnitId());
            String pidlevel = "pid" + (adminUnit.getCountryStructure().getLevel() - 1);
            builder.addRestriction("(c.ownerUnit.address.adminUnit.id = :auId or c.ownerUnit.address.adminUnit." + pidlevel + " = :auId)", qryParams.getAdminUnitId(), qryParams.getAdminUnitId());
        }

        builder.initialize(qryParams);
        QueryResult<CaseItem> data = builder.createQueryResult();

        // prepare result
        TagQueryResult res = new TagQueryResult();
        res.setCount(data.getCount());
        res.setList(data.getList());

        TagData tag = tagService.findOne(qryParams.getTagId(), TagData.class);
        res.setTag(tag);

        return res;
    }
}
