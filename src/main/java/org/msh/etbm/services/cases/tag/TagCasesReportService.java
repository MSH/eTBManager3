package org.msh.etbm.services.cases.tag;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.Tag;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.cases.cases.CaseData;
import org.msh.etbm.services.cases.cases.CaseItem;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service to generate a report about the quantity of cases per tag
 * Created by Mauricio on 09/08/2016.
 */
@Service
public class TagCasesReportService {

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    public QueryResult getTagCases(TagCasesQueryParams qryParams) {
        QueryBuilder<TbCase> builder = queryBuilderFactory.createQueryBuilder(TbCase.class);

        builder.setHqlSelect("select c");
        builder.setEntityAlias("c");
        builder.addDefaultProfile("caseitem", CaseItem.class);

        builder.setHqlJoin("join c.tags t");
        builder.addRestriction("t.id = :tId", qryParams.getTagId());
        builder.addRestriction("c.ownerUnit.id = :unitId", qryParams.getUnitId());
        //TODO: builder.addRestriction("c.ownerUnit.ADMINUNIT = :unitId", qryParams.getAdminUnitId());

        builder.initialize(qryParams);
        return builder.createQueryResult();
    }
}
