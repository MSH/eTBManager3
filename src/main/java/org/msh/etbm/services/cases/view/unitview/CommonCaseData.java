package org.msh.etbm.services.cases.view.unitview;

import org.msh.etbm.db.PersonName;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rmemoria on 3/6/16.
 */
public class CommonCaseData {

    private UUID id;
    private PersonName name;
    private String gender;
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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
