package org.msh.etbm.services.cases.treatment;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.services.cases.treatment.data.PrescriptionData;
import org.msh.etbm.services.cases.treatment.data.PrescriptionPeriod;
import org.msh.etbm.services.cases.treatment.data.TreatmentData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 23/5/16.
 */
@Service
public class TreatmentService {

    /**
     * Return information about a treatment of a given case
     * @param caseId the ID of the case
     * @return
     */
    public TreatmentData get(UUID caseId) {
        TreatmentData data = new TreatmentData();

        Period period = new Period(DateUtils.newDate(2015, 7, 15), DateUtils.newDate(2016, 8, 30));

        data.setPeriod(period);

        data.setProgress(67);

        data.setCategory(new Item<>("FIRST_LINE", "Initial regimen with first line drugs"));

        data.setIniRegimen(new SynchronizableItem(UUID.randomUUID(), "Category IV - 3"));

        data.setRegimen(new SynchronizableItem(null, "Individualized"));

        return data;
    }
}
