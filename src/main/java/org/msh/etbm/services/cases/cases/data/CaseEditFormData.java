package org.msh.etbm.services.cases.cases.data;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Mauricio on 05/09/2016.
 */
public class CaseEditFormData {
    private Map<String, Object> doc;

    private UUID caseId;

    private UUID patientId;

    public Map<String, Object> getDoc() {
        return doc;
    }

    public void setDoc(Map<String, Object> doc) {
        this.doc = doc;
    }

    public UUID getCaseId() {
        return caseId;
    }

    public void setCaseId(UUID caseId) {
        this.caseId = caseId;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }
}
