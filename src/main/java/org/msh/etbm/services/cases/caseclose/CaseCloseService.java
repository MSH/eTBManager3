package org.msh.etbm.services.cases.caseclose;

import org.msh.etbm.commons.Displayable;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.cmdlog.EntityCmdLogHandler;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.admin.tags.CasesTagsUpdateService;
import org.msh.etbm.services.cases.treatment.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Mauricio on 19/07/2016.
 */
@Service
public class CaseCloseService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TreatmentService treatmentService;

    @Autowired
    CasesTagsUpdateService casesTagsUpdateService;

    @Transactional
    @CommandLog(handler = EntityCmdLogHandler.class, type = CommandTypes.CASES_CASE_CLOSE)
    public void closeCase (CaseCloseData data) {
       TbCase tbcase = entityManager.find(TbCase.class, data.getTbcaseId());

       validateClose(tbcase, data);

        if ((tbcase.getTreatmentPeriod() != null) && (!tbcase.getTreatmentPeriod().isEmpty())) {
            treatmentService.cropTreatmentPeriod(tbcase.getId(), new Period(tbcase.getTreatmentPeriod().getIniDate(), data.getOutcomeDate()));
        }

        tbcase.setState(CaseState.CLOSED);
        tbcase.setOutcomeDate(data.getOutcomeDate());
        tbcase.setOutcome(data.getOutcome());
        if (data.getOutcome().equals("OTHER")) {
            tbcase.setOtherOutcome(data.getOtherOutcome());
        }

        // save case changes
        entityManager.persist(tbcase);
        entityManager.flush();

        //casesTagsUpdateService.updateTags(tbcase.getId());
    }

    private void validateClose(TbCase tbcase, CaseCloseData data) {
        Date dt = tbcase.getDiagnosisDate();
        BindingResult errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());;

        if ((dt != null) && (data.getOutcomeDate().before(dt))) {
            errors.rejectValue("outcomeDate", "cases.close.msg1");
        }

        if (tbcase.getTreatmentPeriod() != null && data.getOutcomeDate().after(tbcase.getTreatmentPeriod().getEndDate())) {
            errors.rejectValue("outcomeDate", "cases.close.msg2");
        }

        if (errors.hasErrors()) {
            throw new EntityValidationException(errors);
        }
    }

    @Transactional
    public void reopenCase(UUID tbcaseId) {
        TbCase tbcase = entityManager.find(TbCase.class, tbcaseId);

        if ((tbcase.getTreatmentPeriod() == null) || (tbcase.getTreatmentPeriod().isEmpty())) {
            tbcase.setState(CaseState.WAITING_TREATMENT);
        } else {
            tbcase.setState(CaseState.ONTREATMENT);
        }

        tbcase.setOutcomeDate(null);
        tbcase.setOutcome(null);
        tbcase.setOtherOutcome(null);
        tbcase.setMovedSecondLineTreatment(false);

        // save case changes
        entityManager.persist(tbcase);
        entityManager.flush();

        //casesTagsUpdateService.updateTags(tbcase.getId());
    }
}
