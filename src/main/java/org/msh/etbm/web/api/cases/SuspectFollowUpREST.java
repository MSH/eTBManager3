package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.services.cases.suspectfollowup.SuspectFollowUpService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by msantos on 17/9/16.
 */
@RestController
@RequestMapping("/api/cases/case")
@Authenticated(permissions = {Permissions.CASES_CLOSE})
public class SuspectFollowUpREST {

    @Autowired
    SuspectFollowUpService suspectFollowUpService;

    @RequestMapping(value = "/suspectfollowup/initform/{cla}", method = RequestMethod.GET)
    public FormInitResponse initForm(@PathVariable String cla) {

        if (cla == null || !(cla.equals("NOT_TB") || cla.equals("TB") || cla.equals("DRTB"))) {
            throw new InvalidArgumentException("Suspect confirm classification should be TB, DRTB or NOT TB");
        }

        CaseClassification classification = cla.equals("NOT_TB") ? null : CaseClassification.valueOf(cla);

        return suspectFollowUpService.initForm(classification);
    }

}
