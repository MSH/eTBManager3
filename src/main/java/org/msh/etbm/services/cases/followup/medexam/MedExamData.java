package org.msh.etbm.services.cases.followup.medexam;

import org.msh.etbm.db.enums.MedAppointmentType;
import org.msh.etbm.db.enums.YesNoType;

import java.util.Date;
import java.util.UUID;

/**
 * Created by msantos on 11/7/16.
 */
public class MedExamData {
    private UUID id;
    private Date date;
    private String comments;
    private Double weight;
    private Double height;
    private MedAppointmentType appointmentType;
    private YesNoType usingPrescMedicines;
    private String reasonNotUsingPrescMedicines;
    private String responsible;
    private String positionResponsible;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public MedAppointmentType getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(MedAppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }

    public YesNoType getUsingPrescMedicines() {
        return usingPrescMedicines;
    }

    public void setUsingPrescMedicines(YesNoType usingPrescMedicines) {
        this.usingPrescMedicines = usingPrescMedicines;
    }

    public String getReasonNotUsingPrescMedicines() {
        return reasonNotUsingPrescMedicines;
    }

    public void setReasonNotUsingPrescMedicines(String reasonNotUsingPrescMedicines) {
        this.reasonNotUsingPrescMedicines = reasonNotUsingPrescMedicines;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public String getPositionResponsible() {
        return positionResponsible;
    }

    public void setPositionResponsible(String positionResponsible) {
        this.positionResponsible = positionResponsible;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
