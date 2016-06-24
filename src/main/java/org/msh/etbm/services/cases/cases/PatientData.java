package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.db.enums.Gender;

import java.util.Date;

/**
 * Patient information returned as a response from client request
 *
 * Created by rmemoria on 20/6/16.
 */
public class PatientData extends SynchronizableItem {

    private String name;

    private String middleName;

    private String lastName;

    private String motherName;

    private Date birthDate;

    private Gender gender;

    @Override
    public String getName() {
        return name;
    }

    @Override
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

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
