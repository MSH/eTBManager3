package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.Item;
import org.msh.etbm.db.enums.*;
import org.msh.etbm.services.admin.units.data.UnitData;

import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
public class CaseItem {

    private UUID id;

    private String name;

    private String gender;

    private String caseCode;

    private Item<CaseClassification> classification;

    private Item<DiagnosisType> diagnosisType;

    private Item<CaseState> state;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    public Item<CaseClassification> getClassification() {
        return classification;
    }

    public void setClassification(Item<CaseClassification> classification) {
        this.classification = classification;
    }

    public Item<DiagnosisType> getDiagnosisType() {
        return diagnosisType;
    }

    public void setDiagnosisType(Item<DiagnosisType> diagnosisType) {
        this.diagnosisType = diagnosisType;
    }

    public Item<CaseState> getState() {
        return state;
    }

    public void setState(Item<CaseState> state) {
        this.state = state;
    }

    public UnitData getOwnerUnit() {
        return ownerUnit;
    }

    public void setOwnerUnit(UnitData ownerUnit) {
        this.ownerUnit = ownerUnit;
    }
}
