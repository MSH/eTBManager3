package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Store information of patient personal information
 *
 * @author Ricardo Memoria
 */
@Entity
@Table(name = "patient")
public class Patient extends WorkspaceEntity {

    @Column(length = 100)
    @NotNull
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private String name;

    @Column(length = 100)
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private String middleName;

    @Column(length = 100)
    @PropertyLog(operations = {Operation.NEW, Operation.DELETE})
    private String lastName;

    @Column(length = 50)
    private String securityNumber;

    @Column(length = 100)
    private String motherName;

    @Temporal(TemporalType.DATE)
    @PropertyLog(operations = {Operation.NEW})
    private Date birthDate;

    private Integer recordNumber;

    @NotNull
    @PropertyLog(operations = {Operation.NEW})
    private String gender;

    @Column(length = 50)
    @PropertyLog(messageKey = "form.customId")
    private String customId;

    @OneToMany(mappedBy = "patient")
    @PropertyLog(ignore = true)
    private List<TbCase> cases = new ArrayList<TbCase>();


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getSecurityNumber() {
        return securityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    /**
     * @return the middleName
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * @param middleName the middleName to set
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the cases
     */
    public List<TbCase> getCases() {
        return cases;
    }

    /**
     * @param cases the cases to set
     */
    public void setCases(List<TbCase> cases) {
        this.cases = cases;
    }

    @Override
    public String getDisplayString() {
        return name +
                (middleName != null && !middleName.isEmpty() ? " " + middleName + " " : "") +
                (lastName != null && !lastName.isEmpty() ? " " + lastName + " " : "");
    }
}
