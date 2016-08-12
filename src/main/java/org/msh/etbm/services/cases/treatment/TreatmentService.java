package org.msh.etbm.services.cases.treatment;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.TreatmentDayStatus;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.cases.treatment.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Created by rmemoria on 23/5/16.
 */
@Service
public class TreatmentService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DozerBeanMapper mapper;

    /**
     * Return information about a treatment of a given case
     *
     * @param caseId the ID of the case
     * @return
     */
    public TreatmentData getData(UUID caseId) {
        TbCase tbcase = entityManager.find(TbCase.class, caseId);

        // is on treatment ?
        if (tbcase.getTreatmentPeriod() == null) {
            return null;
        }

        TreatmentData data = new TreatmentData();

        Period period = tbcase.getTreatmentPeriod();

        // set treatment period
        data.setPeriod(period);

        // set the treatment progress
        data.setProgress(calcTreatmentProgress(period));

        // set the regimen
        if (tbcase.getRegimen() != null) {
            data.setRegimen(new SynchronizableItem(tbcase.getRegimen().getId(), tbcase.getRegimen().getName()));
        }

        if (tbcase.getRegimenIni() != null) {
            data.setRegimenIni(new SynchronizableItem(tbcase.getRegimenIni().getId(), tbcase.getRegimenIni().getName()));
        }

        // mount prescriptions
        List<PrescriptionData> prescs = mountPrescriptions(tbcase.getPrescriptions());
        data.setPrescriptions(prescs);

        // mount treatment units
        List<TreatmentUnitData> units = mountTreatmentUnits(tbcase.getTreatmentUnits());
        data.setUnits(units);

        List<MonthlyFollowup> followup = loadTreatmentFollowup(tbcase);
        data.setFollowup(followup);

        return data;
    }

    /**
     * Create the list of treatment followup data per month of the treatment
     * @param tbcase
     * @return
     */
    protected List<MonthlyFollowup> loadTreatmentFollowup(TbCase tbcase) {
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

    /**
     * Create the list of treatment units as the response
     *
     * @param lst
     * @return
     */
    private List<TreatmentUnitData> mountTreatmentUnits(List<TreatmentHealthUnit> lst) {
        List<TreatmentUnitData> res = new ArrayList<>();

        for (TreatmentHealthUnit item : lst) {
            TreatmentUnitData data = new TreatmentUnitData();
            data.setIni(item.getPeriod().getIniDate());
            data.setEnd(item.getPeriod().getEndDate());
            data.setUnit(mapper.map(item.getTbunit(), UnitData.class));

            res.add(data);
        }

        return res;
    }

    /**
     * Mount the response about prescribed medicines
     *
     * @param lst
     * @return
     */
    private List<PrescriptionData> mountPrescriptions(List<PrescribedMedicine> lst) {
        List<PrescriptionData> res = new ArrayList<>();

        for (PrescribedMedicine pm : lst) {
            Product prod = pm.getProduct();
            // search for prescription data based on the product
            Optional<PrescriptionData> opt = res.stream()
                    .filter(it -> it.getProduct().getId().equals(prod.getId()))
                    .findFirst();

            PrescriptionData data;
            if (opt.isPresent()) {
                data = opt.get();
            } else {
                data = new PrescriptionData();
                data.setProduct(new SynchronizableItem(prod.getId(), prod.getName()));
                data.setPeriods(new ArrayList<>());
                res.add(data);
            }

            PrescriptionPeriod p = new PrescriptionPeriod();
            p.setIni(pm.getPeriod().getIniDate());
            p.setEnd(pm.getPeriod().getEndDate());
            p.setComments(pm.getComments());
            p.setDoseUnit(pm.getDoseUnit());
            p.setFrequency(pm.getFrequency());

            data.getPeriods().add(p);
        }

        return res;
    }

    /**
     * Calculate the treatment progress based on the today's date
     *
     * @param period
     * @return
     */
    private int calcTreatmentProgress(Period period) {
        Date today = DateUtils.getDate();

        // the date today is not inside the treatment period ?
        if (!period.isDateInside(today)) {
            return today.before(period.getIniDate()) ? 0 : 100;
        }

        int days = period.getDays();

        Period p = new Period(period.getIniDate(), today);
        int prog = (p.getDays() * 100) / days;

        return prog;
    }

    @Transactional
    public void cropTreatmentPeriod(UUID tbcaseId, Period p) {
        TbCase tbcase = entityManager.find(TbCase.class, tbcaseId);
        int index = 0;

        // crop treatment health units
        while (index < tbcase.getTreatmentUnits().size()) {
            TreatmentHealthUnit hu = tbcase.getTreatmentUnits().get(index);
            // if doesn't intersect, so it's out of the period
            if (!hu.getPeriod().intersect(p)) {
                tbcase.getTreatmentUnits().remove(hu);
                if (entityManager.contains(hu)) {
                    entityManager.remove(hu);
                }
            } else {
                index++;
            }
        }

        // crop prescribed medicines
        index = 0;
        while (index < tbcase.getPrescriptions().size()) {
            PrescribedMedicine pm = tbcase.getPrescriptions().get(index);
            if (!pm.getPeriod().intersect(p)) {
                tbcase.getPrescriptions().remove(pm);
                if (entityManager.contains(pm)) {
                    entityManager.remove(pm);
                }
            } else {
                index++;
            }
        }

        // set the new treatment period, if necessary
        if (tbcase.getTreatmentPeriod().getIniDate().before(p.getIniDate()) || tbcase.getTreatmentPeriod().getEndDate().after(p.getEndDate())) {
            tbcase.setTreatmentPeriod(p);
        }
    }
}
