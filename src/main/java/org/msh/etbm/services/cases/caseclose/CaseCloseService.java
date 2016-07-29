package org.msh.etbm.services.cases.caseclose;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.cases.CaseLogHandler;
import org.msh.etbm.services.cases.cases.CaseData;
import org.msh.etbm.services.cases.tag.AutoGenTagsCasesService;
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
    AutoGenTagsCasesService autoGenTagsCasesService;

    @Autowired
    DozerBeanMapper mapper;

    @Transactional
    @CommandLog(handler = CaseLogHandler.class, type = CommandTypes.CASES_CASE_CLOSE)
    public CaseData closeCase (CaseCloseData data) {
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

        //update case tags
        autoGenTagsCasesService.updateTags(tbcase.getId());

        CaseData caseData = new CaseData();
        mapper.map(tbcase, caseData);

        return caseData;
    }

    @Transactional
    @CommandLog(handler = CaseLogHandler.class, type = CommandTypes.CASES_CASE_REOPEN)
    public CaseData reopenCase(UUID tbcaseId) {
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

        //update case tags
        autoGenTagsCasesService.updateTags(tbcase.getId());

        CaseData caseData = new CaseData();
        mapper.map(tbcase, caseData);

        return caseData;
    }

    private void validateClose(TbCase tbcase, CaseCloseData data) {
        Date dt = tbcase.getDiagnosisDate();
        BindingResult errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

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
}
