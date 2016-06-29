package org.msh.etbm.services.cases.treatment;

import org.dozer.DozerBeanMapper;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.db.entities.PrescribedMedicine;
import org.msh.etbm.db.entities.Product;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.entities.TreatmentHealthUnit;
import org.msh.etbm.services.admin.units.data.UnitData;
import org.msh.etbm.services.cases.treatment.data.PrescriptionData;
import org.msh.etbm.services.cases.treatment.data.PrescriptionPeriod;
import org.msh.etbm.services.cases.treatment.data.TreatmentData;
import org.msh.etbm.services.cases.treatment.data.TreatmentUnitData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        System.out.println(tbcase.getTreatmentUnits().size());
        System.out.println(tbcase.getPrescriptions().size());

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
}
