package org.msh.etbm.services.cases.patient;

import org.msh.etbm.db.PersonName;
import org.msh.etbm.db.enums.Gender;

import java.util.Date;
import java.util.UUID;

/**
 * Patient information returned as a response from client request
 * <p>
 * Created by rmemoria on 20/6/16.
 */
public class PatientDetailedData {

    private UUID id;

    private PersonName name;

    private String motherName;

    private Date birthDate;

    private Gender gender;

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
