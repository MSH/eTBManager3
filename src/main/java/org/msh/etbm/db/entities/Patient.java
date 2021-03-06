package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.PersonName;
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

    @Embedded
    @PropertyLog(addProperties = true)
    private PersonName name;

    @Column(length = 100)
    private String motherName;

    @Temporal(TemporalType.DATE)
    @PropertyLog(operations = {Operation.NEW})
    private Date birthDate;

    @NotNull
    @PropertyLog(operations = {Operation.NEW}, messageKey = "Gender")
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

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public void setName(PersonName name) {
        this.name = name;
    }

    public PersonName getName() {
        return name;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
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
        return name != null ? name.getName() + ' ' + name.getMiddleName() + ' ' + name.getLastName() : "<no name>";
    }
}
