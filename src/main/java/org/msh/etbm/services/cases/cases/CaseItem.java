package org.msh.etbm.services.cases.cases;

import org.msh.etbm.db.enums.*;
import org.msh.etbm.services.admin.units.data.UnitData;

import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
public class CaseItem {

    private UUID id;

    private String name;

    private Gender gender;

    private String caseCode;

    private CaseClassification classification;

    private DiagnosisType diagnosisType;

    private CaseState state;

    private UnitData ownerUnit;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    public CaseClassification getClassification() {
        return classification;
    }

    public void setClassification(CaseClassification classification) {
        this.classification = classification;
    }

    public DiagnosisType getDiagnosisType() {
        return diagnosisType;
    }

    public void setDiagnosisType(DiagnosisType diagnosisType) {
        this.diagnosisType = diagnosisType;
    }

    public CaseState getState() {
        return state;
    }

    public void setState(CaseState state) {
        this.state = state;
    }

    public UnitData getOwnerUnit() {
        return ownerUnit;
    }

    public void setOwnerUnit(UnitData ownerUnit) {
        this.ownerUnit = ownerUnit;
    }
}
