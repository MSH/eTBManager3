package org.msh.etbm.services.cases.patient;

import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.db.PersonName;

import java.util.Date;

/**
 * Created by Mauricio on 30/08/2016.
 */
public class PatientQueryParams extends EntityQueryParams {
    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_ITEM = "item";

    private PersonName name;

    private Date birthDate;

    private String motherName;

    public PersonName getName() {
        return name;
    }

    public void setName(PersonName name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }
}
