package org.msh.etbm.services.cases.patient;

import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.Patient;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.cases.cases.CaseDetailedData;
import org.msh.etbm.services.cases.cases.CaseItem;
import org.msh.etbm.services.cases.cases.CaseQueryParams;
import org.msh.etbm.services.cases.cases.CaseService;
import org.msh.etbm.services.cases.tag.AutoGenTagsCasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by msantos on 01/09/2016.
 */
@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    public QueryResult<PatientSearchItem> searchPatient(PatientQueryParams params) {
        QueryBuilder<TbCase> builder = queryBuilderFactory.createQueryBuilder(TbCase.class);

        builder.addDefaultProfile(PatientQueryParams.PROFILE_DEFAULT, PatientSearchItem.class);
        builder.setEntityAlias("c");

        builder.addLikeRestriction("c.patient.name.name", params.getName().getName());
        builder.addLikeRestriction("c.patient.name.middleName", params.getName().getMiddleName());
        builder.addLikeRestriction("c.patient.name.lastName", params.getName().getLastName());
        builder.addLikeRestriction("c.patient.motherName", params.getMotherName());
        builder.addRestriction("c.patient.birthDate = :birthDate", params.getBirthDate());

        builder.addDefaultOrderByMap("default", "c.patient.name.name, c.patient.name.middleName, c.patient.name.lastName");

        // get most recent case registered
        builder.addRestriction("c.registrationDate = (select max(c2.registrationDate) from TbCase c2 where c2.patient.id = c.patient.id)");

        builder.initialize(params);

        return builder.createQueryResult();
    }
}