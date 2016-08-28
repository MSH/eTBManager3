package org.msh.etbm.services.cases.treatment.followup;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.entities.TreatmentMonitoring;
import org.msh.etbm.db.enums.TreatmentDayStatus;
import org.msh.etbm.services.cases.treatment.TreatmentCmdLogHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service to specifically handle treatment followup functionalities
 *
 * Created by rmemoria on 24/8/16.
 */
@Service
public class TreatmentFollowupService {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Create the list of treatment followup data per month of the treatment
     * @param caseId the ID of the case to get the followup treatment information from
     * @return list of objects {@link MonthlyFollowup} containing information about each month of treatment
     */
    @Transactional
    public List<MonthlyFollowup> loadTreatmentFollowup(UUID caseId) {
        TbCase tbcase = entityManager.find(TbCase.class, caseId);

        Period p = tbcase.getTreatmentPeriod();
        if (p == null) {
            return null;
        }

        // load data about treatment monitoring
        List<TreatmentMonitoring> lst = entityManager.createQuery("from TreatmentMonitoring c  " +
                "where c.tbcase.id = :id ")
                .setParameter("id", tbcase.getId())
                .getResultList();

        List<MonthlyFollowup> followup = new ArrayList<>();

        int months = DateUtils.monthsBetween(p.getIniDate(), p.getEndDate());

        int month = DateUtils.monthOf(p.getIniDate());
        int year = DateUtils.yearOf(p.getIniDate());

        // check if dates are not in the same month
        if (year != DateUtils.yearOf(p.getEndDate()) || (month != DateUtils.monthOf(p.getEndDate()))) {
            // adjust the months
            months++;
        }

        for (int i = 0; i <= months; i++) {
            MonthlyFollowup data = createMonthlyFollowup(lst, year, month);
            followup.add(data);

            if (i == 0) {
                int iniDay = DateUtils.dayOf(p.getIniDate());
                data.setIniDay(iniDay);
            }

            if (i == months) {
                int endDay = DateUtils.dayOf(p.getEndDate());
                data.setEndDay(endDay);
            }

            data.setPlannedDays(DateUtils.daysInAMonth(year, month));

            month++;
            if (month == 12) {
                month = 0;
                year++;
            }
        }

        return followup;
    }


    /**
     * Update the treatment monitoring of a given month/year of a case based on the
     * given request. If there is any validation problem (case not on treatment, for example),
     * an {@link EntityValidationException} will be thrown
     *
     * @param req instance of {@link TreatFollowupUpdateRequest} containing the data to be updated
     */
    @Transactional
    @CommandLog(type = CommandTypes.CASES_TREAT_FOLLOWUP, handler = TreatmentCmdLogHandler.class)
    public void updateTreatmentFollowup(TreatFollowupUpdateRequest req) {
        TbCase tbcase = entityManager.find(TbCase.class, req.getCaseId());
        if (tbcase == null) {
            throw new EntityValidationException(req, "caseId", null, Messages.NOT_VALID);
        }

        // check if case is on treatment
        Period p = tbcase.getTreatmentPeriod();
        if (p == null || !tbcase.isOnTreatment()) {
            throw new EntityValidationException(req, "caseId", "Case is not on treatment", null);
        }

        // check if month and year are inside the treatment period
        // probably there is a better way to test it, but since the initial final month/year can start
        // in arbitrary days, transforming (month, year) to an ordered integer is easier to check
        int monthIni = DateUtils.yearOf(p.getIniDate()) * 100 + DateUtils.monthOf(p.getIniDate());
        int monthEnd = DateUtils.yearOf(p.getEndDate()) * 100 + DateUtils.monthOf(p.getEndDate());
        int m = req.getYear() * 100 + req.getMonth();
        // is month and year inside the period range ?
        if (m < monthIni || m > monthEnd) {
            throw new EntityValidationException(req, "month", null, Messages.NOT_VALID);
        }

        // recover information about treatment monitoring for the giving month and year
        List<TreatmentMonitoring> lst = entityManager.createQuery("from TreatmentMonitoring " +
                "where year = :year and month = :month and tbcase.id = :id")
                .setParameter("year", req.getYear())
                .setParameter("month", req.getMonth())
                .setParameter("id", tbcase.getId())
                .getResultList();

        TreatmentMonitoring tm = lst.size() > 0 ? lst.get(0) : new TreatmentMonitoring();

        // is new ?
        if (tm.getId() == null) {
            tm.setTbcase(tbcase);
            tm.setYear(req.getYear());
            tm.setMonth(req.getMonth());
        }

        for (int i = 1; i <= 31; i++) {
            tm.setDay(i, null);
        }

        for (TreatDayUpdateRequest it: req.getDays()) {
            tm.setDay(it.getDay(), it.getStatus());
        }

        // save the new treatment monitoring
        entityManager.persist(tm);
        entityManager.flush();
    }


    /**
     * Create a new treatment followup by year/month and fill with information about its treatment monitoring
     * for each day of the month
     * @param lst the list containing treatment data
     * @param year the year to get information from
     * @param month the month of the year to get information from
     * @return the instance of {@link MonthlyFollowup} containing treatment followup for the given year/month
     */
    private MonthlyFollowup createMonthlyFollowup(List<TreatmentMonitoring> lst, int year, int month) {
        MonthlyFollowup data = new MonthlyFollowup();
        data.setYear(year);
        data.setMonth(month);

        // search for treatment monitoring
        TreatmentMonitoring item = null;
        for (TreatmentMonitoring tm: lst) {
            if (tm.getYear() == year && tm.getMonth() == month) {
                item = tm;
                break;
            }
        }

        // was found treatment monitoring data for the given month/year
        if (item != null) {
            List<FollowupDay> days = new ArrayList<>();

            int numDays = DateUtils.daysInAMonth(year, month);

            for (int day = 1; day <= numDays; day++) {
                TreatmentDayStatus status = item.getDay(day);
                if (status != null) {
                    FollowupDay fd = new FollowupDay();
                    fd.setDay(day);
                    fd.setStatus(status);
                    days.add(fd);
                }
            }

            if (days.size() > 0) {
                data.setDays(days);
            }
        }

        return data;
    }

}
