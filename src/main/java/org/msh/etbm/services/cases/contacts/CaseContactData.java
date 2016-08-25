package org.msh.etbm.services.cases.contacts;

import org.msh.etbm.db.enums.Gender;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Mauricio on 01/08/2016.
 */
public class CaseContactData {

    private UUID id;

    private String name;

    private Gender gender;

    private String age;

    private Date dateOfExamination;

    private String contactType;

    private boolean examinated;

    private String conduct;

    private String comments;

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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Date getDateOfExamination() {
        return dateOfExamination;
    }

    public void setDateOfExamination(Date dateOfExamination) {
        this.dateOfExamination = dateOfExamination;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public boolean isExaminated() {
        return examinated;
    }

    public void setExaminated(boolean examinated) {
        this.examinated = examinated;
    }

    public String getConduct() {
        return conduct;
    }

    public void setConduct(String conduct) {
        this.conduct = conduct;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
