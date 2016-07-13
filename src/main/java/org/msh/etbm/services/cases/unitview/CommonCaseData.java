package org.msh.etbm.services.cases.unitview;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rmemoria on 3/6/16.
 */
public class CommonCaseData {

    private UUID id;
    private String name;
    private String gender;
    private Date registrationDate;

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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
