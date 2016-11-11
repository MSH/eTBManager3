package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseClassification;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Validates a tbcase
 * Created by Mauricio on 20/10/2016.
 */
@Component
public class CaseValidator {

    /**
     * Validates tbcase and interrupts its saving if a validation issue is found
     * @param tbCase
     */
    protected void validateTbCase(TbCase tbCase, Map<String, Object> doc) {

        // checks if bmu tb register date is before registration date
        if (CaseClassification.DRTB.equals(tbCase.getClassification()) && tbCase.getLastBmuDateTbRegister() != null
                && tbCase.getLastBmuDateTbRegister().compareTo(tbCase.getRegistrationDate()) >= 0) {
            throw new EntityValidationException(doc, "tbcase.lastBmuDateTbRegister", null, "cases.details.valerror2");
        }

    }

}
