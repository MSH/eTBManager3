package org.msh.etbm.services.cases.cases;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityServiceContext;
import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryBuilder;
import org.msh.etbm.commons.entities.query.QueryBuilderFactory;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.db.entities.Patient;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.cases.tag.AutoGenTagsCasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
@Service
public class CaseServiceImpl extends EntityServiceImpl<TbCase, CaseQueryParams> implements CaseService {

    @Autowired
    AutoGenTagsCasesService autoGenTagsCasesService;

    @Autowired
    QueryBuilderFactory queryBuilderFactory;

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    FormService formService;

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

    @Override
    public FormInitResponse getReadOnlyForm(UUID id) {
        TbCase tbcase = super.findEntity(id);

        String formid = "newnotif-";
        formid = formid.concat(tbcase.getDiagnosisType().name().toLowerCase()).concat("-");
        formid = formid.concat(tbcase.getClassification().name().toLowerCase());

        CaseDetailedData caseData = mapper.map(tbcase, CaseDetailedData.class);

        return formService.init(formid, caseData, true);
    }
}