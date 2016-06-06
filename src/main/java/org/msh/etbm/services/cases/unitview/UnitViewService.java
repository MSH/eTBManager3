package org.msh.etbm.services.cases.unitview;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.Patient;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.*;
import org.msh.etbm.services.admin.tags.TagData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service that returns the data to create the unit view of the case management module
 * Created by rmemoria on 3/6/16.
 */
@Service
public class UnitViewService {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public UnitViewData getUnitView(UUID unitId) {
        UnitViewData data = new UnitViewData();

        loadCases(unitId, data);

        loadTags(unitId, data);

        return data;
    }

    /**
     * Load the cases to load into unit view
     * @param unitId the unit ID to get the cases from
     * @param data the data to receive the cases
     */
    private void loadCases(UUID unitId, UnitViewData data) {
        List<TbCase> lst = entityManager.createQuery("from TbCase c " +
                "join fetch c.patient where c.ownerUnit.id = :unitId " +
                "and c.state in (:st1, :st2)")
                .setParameter("unitId", unitId)
                .setParameter("st1", CaseState.ONTREATMENT)
                .setParameter("st2", CaseState.WAITING_TREATMENT)
                .getResultList();

        data.setPresumptives(new ArrayList<>());
        data.setDrtbCases(new ArrayList<>());
        data.setTbCases(new ArrayList<>());

        for (TbCase tbcase: lst) {

            // is a case a presumptive one?
            if (tbcase.getDiagnosisType() == DiagnosisType.SUSPECT) {
                data.getPresumptives().add(createPresumptiveData(tbcase));
            } else {
                // get confirmed case data
                ConfirmedCaseData caseData = createConfirmedData(tbcase);

                // put case in the right list
                if (tbcase.getClassification() == CaseClassification.TB) {
                    data.getTbCases().add( caseData );
                } else {
                    data.getDrtbCases().add( caseData );
                }
            }
        }
    }

    private PresumptiveCaseData createPresumptiveData(TbCase tbcase) {
        PresumptiveCaseData data = createCaseData(tbcase, PresumptiveCaseData.class);

        data.setCaseNumber("TO BE DONE");
        data.setMicroscopyResult(MicroscopyResult.NEGATIVE);
        data.setXpertResult(XpertResult.NO_RESULT);

        return data;
    }

    private ConfirmedCaseData createConfirmedData(TbCase tbcase) {
        ConfirmedCaseData data = createCaseData(tbcase, ConfirmedCaseData.class);

        data.setCaseNumber("TO BE DONE");
        data.setInfectionSite(tbcase.getInfectionSite());
        data.setRegistrationGroup(new Item<String>(tbcase.getRegistrationGroup(), tbcase.getRegistrationGroup()));

        Date ini = tbcase.getTreatmentPeriod().getIniDate();

        if (ini != null) {
            data.setIniTreatmentDate(tbcase.getTreatmentPeriod().getIniDate());

            // calculate the treatment progress based on the current date
            Period p = new Period(ini, new Date());

            int daysTreatment = tbcase.getTreatmentPeriod().getDays();

            int prog = daysTreatment > 0 ? (p.getDays() * 100) / daysTreatment * 100 : 0;

            // limit in case the treatment should have finished
            if (prog > 100) {
                prog = 100;
            }

            data.setTreatmentProgress(prog);
        }

        return data;
    }

    private <K extends CommonCaseData> K createCaseData(TbCase tbcase, Class<K> clazz) {
        K data =  ObjectUtils.newInstance(clazz);

        Patient p = tbcase.getPatient();

        data.setId(tbcase.getId());
        data.setName(p.getDisplayString());
        data.setGender(p.getGender());
        data.setRegistrationDate(tbcase.getRegistrationDate());

        return data;
    }


    private void loadTags(UUID unitId, UnitViewData data) {
        List<CaseTagData> tags = new ArrayList<>();

        if (tags.size() > 0) {
            data.setTags(tags);
        }
    }
}
