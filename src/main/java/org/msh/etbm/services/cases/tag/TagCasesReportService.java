package org.msh.etbm.services.cases.tag;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.AdministrativeUnit;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.cases.cases.CaseItem;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Service to generate a report about the quantity of cases per tag
 * Created by Mauricio on 09/08/2016.
 */
@Service
public class TagCasesReportService {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @PersistenceContext
    EntityManager entityManager;

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
        return builder.createQueryResult();
    }
}
