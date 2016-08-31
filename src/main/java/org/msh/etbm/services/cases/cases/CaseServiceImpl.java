package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Patient;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.cases.tag.AutoGenTagsCasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by msantos on 26/3/16.
 */
@Service
public class CaseServiceImpl extends EntityServiceImpl<TbCase, CaseQueryParams> implements CaseService {

    @Autowired
    AutoGenTagsCasesService autoGenTagsCasesService;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Override
    public String getCommandType() {
        return CommandTypes.CASES_CASE;
    }

    @Override
    protected void buildQuery(QueryBuilder<TbCase> builder, CaseQueryParams queryParams) {
        // profiles
        builder.addProfile(CaseQueryParams.PROFILE_DETAILED, CaseDetailedData.class);
        builder.addDefaultProfile(CaseQueryParams.PROFILE_ITEM, CaseItem.class);
    }

    @Override
    protected void afterDelete(EntityServiceContext<TbCase> context, ServiceResult res) {
        // removes patient from database if this patient don't have any other case registered
        Patient patient = context.getEntity().getPatient();

        Long count = (Long)getEntityManager()
                .createQuery("select count(*) from TbCase c where c.patient.id = :id")
                .setParameter("id", patient.getId())
                .getSingleResult();

        if (count == 0) {
            getEntityManager().remove(patient);
        }
    }

    public QueryResult<PatientSearchItem> searchPatient(PatientSearchQueryParams params) {
        QueryBuilder<TbCase> builder = queryBuilderFactory.createQueryBuilder(TbCase.class);

        builder.addDefaultProfile(PatientSearchQueryParams.PROFILE_DEFAULT, PatientSearchItem.class);
        builder.setEntityAlias("c");

        builder.addLikeRestriction("c.patient.name", params.getName());
        builder.addLikeRestriction("c.patient.middleName", params.getMiddleName());
        builder.addLikeRestriction("c.patient.lastName", params.getLastName());
        builder.addLikeRestriction("c.patient.motherName", params.getMotherName());
        builder.addRestriction("c.patient.birthDate = :birthDate", params.getBirthDate());

        builder.addDefaultOrderByMap("default", "c.patient.name, c.patient.middleName, c.patient.lastName");

        // get most recent case registered
        builder.addRestriction("c.registrationDate = (select max(c2.registrationDate) from TbCase c2 where c2.patient.id = c.patient.id)");

        builder.initialize(params);

        return builder.createQueryResult();
    }
}