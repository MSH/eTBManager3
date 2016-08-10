package org.msh.etbm.services.cases.tag;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.Tag;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.cases.cases.CaseData;
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

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    public QueryResult getTagCasesWorkspace(TagCasesQueryParams qryParams) {
        return new QueryResult();
    }

    /*public QueryResult getTagCasesByUnit(TagCasesQueryParams qryParams) {
        QueryResult<CaseData> res = new QueryResult<>();
        res.setList(new ArrayList<>());

        List<TbCase> list = (List<TbCase>)entityManager.createQuery("select c from TbCase c join c.tags t where t.id = :tId and c.ownerUnit.id = :uId")
                                                        .setParameter("tId", qryParams.getTagId())
                                                        .setParameter("uId", qryParams.getUnitId())
                                                        .getResultList();

        res.setCount(list.size());

        for (TbCase c : list) {
            res.getList().add(mapper.map(c, CaseData.class));
        }

        return res;
    }*/

    public QueryResult getTagCasesByUnit(TagCasesQueryParams qryParams) {
        QueryBuilder<TbCase> builder = queryBuilderFactory.createQueryBuilder(TbCase.class);

        builder.setHqlSelect("select c");
        builder.setEntityAlias("c");
        builder.addDefaultProfile("casedata", CaseData.class);

        builder.setHqlJoin("join c.tags t");
        builder.addRestriction("t.id = :tId and c.ownerUnit.id = :unitId", qryParams.getTagId(), qryParams.getUnitId());

        builder.initialize(qryParams);
        return builder.createQueryResult();
    }
}
