package org.msh.etbm.services.cases.followup.medexam;

import org.msh.etbm.db.enums.MedAppointmentType;
import org.msh.etbm.db.enums.YesNoType;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by msantos on 11/7/16.
 */
public class MedExamFormData {
    private Optional<Date> date;
    private Optional<String> comments;
    private Optional<Double> weight;
    private Optional<Double> height;
    private Optional<MedAppointmentType> appointmentType;
    private Optional<YesNoType> usingPrescMedicines;
    private Optional<String> reasonNotUsingPrescMedicines;
    private Optional<String> responsible;
    private Optional<String> positionResponsible;
    private UUID tbcaseId;

    public Optional<Date> getDate() {
        return date;
    }

    public void setDate(Optional<Date> date) {
        this.date = date;
    }

    public Optional<String> getComments() {
        return comments;
    }

    public void setComments(Optional<String> comments) {
        this.comments = comments;
    }

    public Optional<Double> getWeight() {
        return weight;
    }

    public void setWeight(Optional<Double> weight) {
        this.weight = weight;
    }

    public Optional<Double> getHeight() {
        return height;
    }

    public void setHeight(Optional<Double> height) {
        this.height = height;
    }

    public Optional<MedAppointmentType> getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(Optional<MedAppointmentType> appointmentType) {
        this.appointmentType = appointmentType;
    }

    public Optional<YesNoType> getUsingPrescMedicines() {
        return usingPrescMedicines;
    }

    public void setUsingPrescMedicines(Optional<YesNoType> usingPrescMedicines) {
        this.usingPrescMedicines = usingPrescMedicines;
    }

    public Optional<String> getReasonNotUsingPrescMedicines() {
        return reasonNotUsingPrescMedicines;
    }

    public void setReasonNotUsingPrescMedicines(Optional<String> reasonNotUsingPrescMedicines) {
        this.reasonNotUsingPrescMedicines = reasonNotUsingPrescMedicines;
    }

    public Optional<String> getResponsible() {
        return responsible;
    }

    public void setResponsible(Optional<String> responsible) {
        this.responsible = responsible;
    }

    public Optional<String> getPositionResponsible() {
        return positionResponsible;
    }

    public void setPositionResponsible(Optional<String> positionResponsible) {
        this.positionResponsible = positionResponsible;
    }

    public UUID getTbcaseId() {
        return tbcaseId;
    }

    public void setTbcaseId(UUID tbcaseId) {
        this.tbcaseId = tbcaseId;
    }
}
