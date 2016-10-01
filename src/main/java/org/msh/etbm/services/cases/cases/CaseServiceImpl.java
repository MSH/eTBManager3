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
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.db.RecordData;
import org.msh.etbm.db.entities.Patient;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.services.cases.tag.AutoGenTagsCasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    @Autowired
    ModelDAOFactory factory;

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
        // mount data
        HashMap<String, Object> data = new HashMap<>();

        ModelDAO tbcaseDao = factory.create("tbcase");
        RecordData resTbcase = tbcaseDao.findOne(id, true);

        ModelDAO patientDao = factory.create("patient");
        RecordData resPatient = patientDao.findOne((UUID)resTbcase.getValues().get("patient"), true);

        data.put("tbcase", resTbcase.getValues());
        data.put("patient", resPatient.getValues());

        // mount form name
        DiagnosisType diag = (DiagnosisType) resTbcase.getValues().get("diagnosisType");
        CaseClassification cla = (CaseClassification) resTbcase.getValues().get("classification");

        String formid;
        if (diag.equals(DiagnosisType.CONFIRMED)) {
            formid = "case-display/confirmed-";
            formid = formid.concat(cla.name().toLowerCase());
        } else {
            formid = "case-display/suspect";
        }

        return formService.init(formid, data, true);
    }

    @Override
    public FormInitResponse getEditForm(UUID id) {
        // mount data
        HashMap<String, Object> data = new HashMap<>();

        ModelDAO tbcaseDao = factory.create("tbcase");
        RecordData resTbcase = tbcaseDao.findOne(id, true);

        ModelDAO patientDao = factory.create("patient");
        RecordData resPatient = patientDao.findOne((UUID)resTbcase.getValues().get("patient"), true);

        data.put("tbcase", resTbcase.getValues());
        data.put("patient", resPatient.getValues());

        // mount form name
        DiagnosisType diag = (DiagnosisType) resTbcase.getValues().get("diagnosisType");
        CaseClassification cla = (CaseClassification) resTbcase.getValues().get("classification");

        // generate form id
        String formid = "case-update/";
        formid = formid.concat(diag.name().toLowerCase()).concat("-");
        formid = formid.concat(cla.name().toLowerCase());

        return formService.init(formid, data, false);
    }
}