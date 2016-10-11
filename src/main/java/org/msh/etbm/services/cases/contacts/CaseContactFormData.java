package org.msh.etbm.services.cases.contacts;

import org.msh.etbm.db.enums.Gender;
import org.msh.etbm.services.cases.CaseEntityFormData;

import java.util.Date;
import java.util.Optional;

/**
 * Created by Mauricio on 01/08/2016.
 */
public class CaseContactFormData extends CaseEntityFormData {

    private Optional<String> name;

    private Optional<Gender> gender;

    private Optional<String> age;

    private Optional<Date> dateOfExamination;

    private Optional<String> contactType;

    private Optional<Boolean> examinated;

    private Optional<String> conduct;

    private Optional<String> comments;

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<Gender> getGender() {
        return gender;
    }

    public void setGender(Optional<Gender> gender) {
        this.gender = gender;
    }

    public Optional<String> getAge() {
        return age;
    }

    public void setAge(Optional<String> age) {
        this.age = age;
    }

    public Optional<Date> getDateOfExamination() {
        return dateOfExamination;
    }

    public void setDateOfExamination(Optional<Date> dateOfExamination) {
        this.dateOfExamination = dateOfExamination;
    }

    public Optional<String> getContactType() {
        return contactType;
    }

    public void setContactType(Optional<String> contactType) {
        this.contactType = contactType;
    }

    public Optional<Boolean> getExaminated() {
        return examinated;
    }

    public void setExaminated(Optional<Boolean> examinated) {
        this.examinated = examinated;
    }

    public Optional<String> getConduct() {
        return conduct;
    }

    public void setConduct(Optional<String> conduct) {
        this.conduct = conduct;
    }

    public Optional<String> getComments() {
        return comments;
    }

    public void setComments(Optional<String> comments) {
        this.comments = comments;
    }
}
