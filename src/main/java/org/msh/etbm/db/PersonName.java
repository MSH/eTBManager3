package org.msh.etbm.db;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Created by rmemoria on 20/8/16.
 */
@Embeddable
public class PersonName {

    @PropertyLog(operations = {Operation.NEW, Operation.DELETE}, messageKey = "Patient.name")
    @Column(length = 100)
    @NotNull
    private String name;

    @PropertyLog(operations = {Operation.NEW, Operation.DELETE}, messageKey = "Patient.middleName")
    @Column(length = 100)
    private String middleName;

    @PropertyLog(operations = {Operation.NEW, Operation.DELETE}, messageKey = "Patient.lastName")
    @Column(length = 100)
    private String lastName;

    public PersonName(String name, String middleName, String lastName) {
        this.name = name;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "PersonName{" +
                "name='" + name + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public PersonName() {
        super();
    }

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
}
