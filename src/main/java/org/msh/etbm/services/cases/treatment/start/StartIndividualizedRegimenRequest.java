package org.msh.etbm.services.cases.treatment.start;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Request containing data to start an individualized regimen treatment for a case
 *
 * Created by rmemoria on 31/8/16.
 */
public class StartIndividualizedRegimenRequest {

    /**
     * The ID of the unit to start the treatment. If null, the owner unit of the case
     * will be used instead
     */
    private UUID unitId;

    /**
     * The ID of the case
     */
    @NotNull
    private UUID caseId;

    /**
     * The regimen ID
     */
    @NotNull
    private UUID regimenId;

    /**
     * The initial date of the treatment
     */
    @NotNull
    private Date iniDate;
}
