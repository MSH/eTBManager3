package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.CaseEntity;
import org.msh.etbm.db.enums.Gender;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "casecontact")
public class CaseContact extends CaseEntity {

    @PropertyLog(operations = {Operation.ALL})
    private String name;

    @PropertyLog(messageKey = "Gender")
    private Gender gender;

    @PropertyLog(messageKey = "TbCase.age", operations = {Operation.NEW})
    private String age;

    //VR: adding 'date of examination'
    private Date dateOfExamination;

    @Column(length = 50)
    private String contactType;

    private boolean examinated;

    @Column(length = 50)
    private String conduct;

    @Lob
    @PropertyLog(messageKey = "global.comments")
    private String comments;


    // Ricardo: TEMPORARY UNTIL A SOLUTION IS FOUND. Just to attend a request from the XML data model to
    // map an XML node to a property in the model
    @Transient
    private Integer clientId;

    /**
     * @return
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * @param clientId
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @return the age
     */
    public String getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * @return the contactType
     */
    public String getContactType() {
        return contactType;
    }

    /**
     * @param contactType the contactType to set
     */
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    /**
     * @return the examinated
     */
    public boolean isExaminated() {
        return examinated;
    }

    /**
     * @param examinated the examinated to set
     */
    public void setExaminated(boolean examinated) {
        this.examinated = examinated;
    }

    /**
     * @return the conduct
     */
    public String getConduct() {
        return conduct;
    }

    /**
     * @param conduct the conduct to set
     */
    public void setConduct(String conduct) {
        this.conduct = conduct;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return dateOfExamination
     */
    public Date getDateOfExamination() {
        return dateOfExamination;
    }

    /**
     * @param dateOfExamination the comments to set
     */
    public void setDateOfExamination(Date dateOfExamination) {
        this.dateOfExamination = dateOfExamination;
    }

}
