package org.msh.etbm.web.api.sys;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.db.MessageKey;
import org.msh.etbm.db.enums.*;
import org.msh.etbm.services.cases.followup.data.FollowUpType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Provide global list options used throughout the system. The main reason
 * of this service is to expose all standard lists to the client side
 * <p>
 * Created by rmemoria on 10/12/15.
 */
@Service
public class GlobalListsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalListsService.class);

    @Autowired
    Messages messages;


    // available lists to be sent to the client
    private static final Class[] lists = {
            MedicineLine.class,
            MedicineCategory.class,
            CaseClassification.class,
            CaseState.class,
            DiagnosisType.class,
            ValidationState.class,
            CommandAction.class,
            FollowUpType.class,
            MedAppointmentType.class,
            VisualAppearance.class,
            SampleType.class,
            MicroscopyResult.class,
            CultureResult.class,
            XpertRifResult.class,
            XpertResult.class,
            XRayEvolution.class,
            HIVResult.class,
            ExamStatus.class,
            DstResult.class,
            PrevTBTreatmentOutcome.class
    };

    /**
     * Return the options of all lists supported by the system
     *
     * @return
     */
    public Map<String, Map<String, String>> getLists() {
        Map<String, Map<String, String>> res = new HashMap<>();

        for (Class clazz : lists) {
            String name = clazz.getSimpleName();
            Map<String, String> options = getOptions(clazz);

            res.put(name, options);
        }

        putClassificationDiagnosisOptions(res);

        return res;
    }

    /**
     * Return the options from the given class (actually, just enums are supported)
     *
     * @param clazz the enum class to get options from
     * @return the list of options from the enumeration
     */
    private Map<String, String> getOptions(Class clazz) {
        if (!clazz.isEnum()) {
            throw new RuntimeException("Type not supported: " + clazz);
        }

        Object[] vals = clazz.getEnumConstants();

        Map<String, String> opts = new HashMap<>();

        for (Object val : vals) {
            String messageKey;

            if (val instanceof MessageKey) {
                messageKey = ((MessageKey) val).getMessageKey();
            } else {
                messageKey = clazz.getSimpleName() + '.' + val.toString();
            }

            String message = messages.get(messageKey);

            opts.put(val.toString(), message);
        }

        return opts;
    }

    /**
     * Puts the different messages combination of case classification and diagnosis types on the param
     * @param res object that will have the values put
     */
    private void putClassificationDiagnosisOptions(Map<String, Map<String, String>> res) {
        Map<String, String> opts = new HashMap<>();
        opts.put(DiagnosisType.SUSPECT.toString(), messages.get(CaseClassification.TB.getKeySuspect()));
        opts.put(DiagnosisType.CONFIRMED.toString(), messages.get(CaseClassification.TB.getKeyConfirmed()));
        res.put("CaseClassificationTB", opts);

        opts = new HashMap<>();
        opts.put(DiagnosisType.SUSPECT.toString(), messages.get(CaseClassification.DRTB.getKeySuspect()));
        opts.put(DiagnosisType.CONFIRMED.toString(), messages.get(CaseClassification.DRTB.getKeyConfirmed()));
        res.put("CaseClassificationDRTB", opts);

        opts = new HashMap<>();
        opts.put(DiagnosisType.SUSPECT.toString(), messages.get(CaseClassification.NTM.getKeySuspect()));
        opts.put(DiagnosisType.CONFIRMED.toString(), messages.get(CaseClassification.NTM.getKeyConfirmed()));
        res.put("CaseClassificationNTM", opts);
    }

}
