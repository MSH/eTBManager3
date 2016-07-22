package org.msh.etbm.services.cases.caseclose;

import org.dozer.DozerBeanMapper;
import org.hibernate.Hibernate;
import org.msh.etbm.commons.Displayable;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandType;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.cmdlog.EntityCmdLogHandler;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLogUtils;
import org.msh.etbm.commons.objutils.Diffs;
import org.msh.etbm.commons.objutils.DiffsUtils;
import org.msh.etbm.commons.objutils.ObjectValues;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.admin.tags.CasesTagsUpdateService;
import org.msh.etbm.services.cases.cases.CaseData;
import org.msh.etbm.services.cases.treatment.TreatmentService;
import org.msh.etbm.web.api.StandardResult;
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

    @Autowired
    DozerBeanMapper mapper;

    @Transactional
    @CommandLog(handler = EntityCmdLogHandler.class, type = CommandTypes.CASES_CASE_CLOSE)
    public ServiceResult closeCase (CaseCloseData data) {
        TbCase tbcase = entityManager.find(TbCase.class, data.getTbcaseId());

       // get initial state
       ObjectValues prevVals = createValuesToLog(tbcase, Operation.EDIT);

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

        // get final state
        ObjectValues newVals = createValuesToLog(tbcase, Operation.EDIT);

        // save case changes
        entityManager.persist(tbcase);
        entityManager.flush();

        //update case tags
        //casesTagsUpdateService.updateTags(tbcase.getId());

        return createResult(tbcase, CommandTypes.CASES_CASE_CLOSE, prevVals, newVals);
    }

    @Transactional
    @CommandLog(handler = EntityCmdLogHandler.class, type = CommandTypes.CASES_CASE_REOPEN)
    public ServiceResult reopenCase(UUID tbcaseId) {
        TbCase tbcase = entityManager.find(TbCase.class, tbcaseId);

        // get initial state
        ObjectValues prevVals = createValuesToLog(tbcase, Operation.EDIT);

        if ((tbcase.getTreatmentPeriod() == null) || (tbcase.getTreatmentPeriod().isEmpty())) {
            tbcase.setState(CaseState.WAITING_TREATMENT);
        } else {
            tbcase.setState(CaseState.ONTREATMENT);
        }

        tbcase.setOutcomeDate(null);
        tbcase.setOutcome(null);
        tbcase.setOtherOutcome(null);
        tbcase.setMovedSecondLineTreatment(false);

        // get final state
        ObjectValues newVals = createValuesToLog(tbcase, Operation.EDIT);

        // save case changes
        entityManager.persist(tbcase);
        entityManager.flush();

        //update case tags
        //casesTagsUpdateService.updateTags(tbcase.getId());

        return createResult(tbcase, CommandTypes.CASES_CASE_REOPEN, prevVals, newVals);
    }

    private void validateClose(TbCase tbcase, CaseCloseData data) {
        /*Date dt = tbcase.getDiagnosisDate();
        BindingResult errors = new BeanPropertyBindingResult(data, data.getClass().getSimpleName());

        if ((dt != null) && (data.getOutcomeDate().before(dt))) {
            errors.rejectValue("outcomeDate", "cases.close.msg1");
        }

        if (tbcase.getTreatmentPeriod() != null && data.getOutcomeDate().after(tbcase.getTreatmentPeriod().getEndDate())) {
            errors.rejectValue("outcomeDate", "cases.close.msg2");
        }

        if (errors.hasErrors()) {
            throw new EntityValidationException(errors);
        }*/

        Date dt = tbcase.getDiagnosisDate();

        if ((dt != null) && (data.getOutcomeDate().before(dt))) {
            throw new EntityValidationException(data, "outcomeDate", null, "cases.close.msg1");
        }

        if (tbcase.getTreatmentPeriod() != null && data.getOutcomeDate().after(tbcase.getTreatmentPeriod().getEndDate())) {
            throw new EntityValidationException(data, "outcomeDate", null, "cases.close.msg2");
        }
    }

    /**
     * Create the result to be returned by the create, update or delete operation
     *
     * @param entity the entity involved in the operation
     * @return instance of {@link ServiceResult}
     */
    protected ServiceResult createResult(TbCase entity, String cmdPath, ObjectValues prevVals, ObjectValues newVals) {
        ServiceResult res = new ServiceResult();

        res.setId(entity.getId());
        res.setEntityClass(TbCase.class);

        res.setCommandType(CommandTypes.get(cmdPath));
        res.setEntityName(entity.getDisplayString());

        Diffs diffs = DiffsUtils.generateDiffsFromValues(prevVals, newVals);
        res.setLogDiffs(diffs);
        res.setOperation(Operation.ALL);

        return res;
    }

    /**
     * Create the list of values to be logged
     *
     * @param entity
     * @param oper
     * @return
     */
    protected ObjectValues createValuesToLog(TbCase entity, Operation oper) {
        Class pureClass = Hibernate.getClass(entity);
        return PropertyLogUtils.generateLog(entity, pureClass, oper);
    }
}
