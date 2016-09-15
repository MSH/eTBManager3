package org.msh.etbm.db.entities;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.db.CaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

/**
 * Store information about a medicine prescribed to a case
 *
 * @author Ricardo Memoria
 */
@Entity
@Table(name = "prescribedmedicine")
public class PrescribedMedicine extends CaseEntity {

    /**
     * Medicine prescribed
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull
    private Product product;

    /**
     * Period of administration of the medicine prescribed
     */
    @Embedded
    private Period period = new Period();

    /**
     * Dose unit prescribed for the period
     */
    private int doseUnit;

    /**
     * Weekly frequency of administration of the medicine
     */
    private int frequency;

    /**
     * Optional comments entered by the user
     */
    @Lob
    private String comments;


    /**
     * Check if has any comment to the prescribed medicine
     *
     * @return true if has comments
     */
    public boolean isHasComments() {
        return (comments != null) && (!comments.isEmpty());
    }



    @Override
    public String toString() {
        return ((product != null) && (period != null) ? period.toString() + " - " + product.toString() : null);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getDoseUnit() {
        return doseUnit;
    }

    public void setDoseUnit(int doseUnit) {
        this.doseUnit = doseUnit;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


    public Period getPeriod() {
        return period;
    }


    public void setPeriod(Period period) {
        this.period = period;
    }

}
