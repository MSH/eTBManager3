package org.msh.etbm.services.cases.followup.medexam;

import org.msh.etbm.db.enums.MedAppointmentType;
import org.msh.etbm.services.cases.CaseEventFormData;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by msantos on 11/7/16.
 */
public class MedExamFormData extends CaseEventFormData {

    private Optional<Double> weight;
    private Optional<Double> height;
    private Optional<MedAppointmentType> appointmentType;
    private Optional<Boolean> usingPrescMedicines;
    private Optional<String> reasonNotUsingPrescMedicines;
    private Optional<String> responsible;
    private Optional<String> positionResponsible;
    private Optional<UUID> tbcaseId;

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

    public Optional<Boolean> getUsingPrescMedicines() {
        return usingPrescMedicines;
    }

    public void setUsingPrescMedicines(Optional<Boolean> usingPrescMedicines) {
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

    public Optional<UUID> getTbcaseId() {
        return tbcaseId;
    }

    public void setTbcaseId(Optional<UUID> tbcaseId) {
        this.tbcaseId = tbcaseId;
    }
}
