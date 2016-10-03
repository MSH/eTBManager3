package org.msh.etbm.services.cases.cases.data;

import org.msh.etbm.commons.Item;
import org.msh.etbm.db.PersonName;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.services.admin.units.data.UnitData;

import java.util.Date;
import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
public class CaseItem {

    private UUID id;

    private PersonName name;

    private String gender;

    private String caseCode;

    private Item<CaseClassification> classification;

    private Item<DiagnosisType> diagnosisType;

    private Item<CaseState> state;

    private UnitData ownerUnit;

    private Date registrationDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PersonName getName() {
        return name;
    }

    public void setName(PersonName name) {
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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
