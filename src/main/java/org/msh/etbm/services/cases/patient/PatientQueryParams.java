package org.msh.etbm.services.cases.patient;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

import java.util.Date;

/**
 * Created by Mauricio on 30/08/2016.
 */
public class PatientQueryParams extends EntityQueryParams {
    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_ITEM = "item";

    private String name;

    private String middleName;

    private String lastName;

    private Date birthDate;

    private String motherName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
