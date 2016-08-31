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


    /**
     * Return the month of treatment according to the beginning of the treatment and the initial date of the period
     *
     * @return month of treatment, from 0 (=January) to 11 (=December)
     */
    public Integer getIniMonth() {
        if ((period == null) || (period.isEmpty()) || (getTbcase() == null)) {
            return null;
        }

        return getTbcase().getMonthTreatment(period.getIniDate()) - 1;
    }


    /**
     * Return number of months in the period
     *
     * @return integer value with the number of months between the initial and final dates of the period
     */
    public Integer getMonths() {
        return (period != null ? period.getMonths() : null);
    }


    /**
     * Change the initial month of the period
     *
     * @param month
     */
    public void setIniMonth(Integer month) {
        TbCase tbcase = getTbcase();
        if (period.isEmpty()) {
            if (tbcase != null) {
                period = new Period(tbcase.getTreatmentPeriod());
            }

            if (period.getIniDate() == null) {
                period.set(new Date(), new Date());
            }
        }

        Date dt = DateUtils.incMonths(period.getIniDate(), month - 1);
        period.movePeriod(dt);
    }


    /**
     * Specify the number of months of the period
     *
     * @param months
     */
    public void setMonths(Integer months) {
        TbCase tbcase = getTbcase();

        if (period == null) {
            return;
        }

        if (period.getIniDate() == null) {
            if (tbcase != null) {
                period.setIniDate(tbcase.getTreatmentPeriod().getIniDate());
            } else {
                period.setIniDate(new Date());
            }
        }

        // Solves a bug that happens every end of January (pay attention in other dates)
        // on the creation of a individualized treatment if there is only one month of medicine precription*/
        Calendar c = Calendar.getInstance();
        c.setTime(period.getIniDate());
        if ((c.get(Calendar.MONTH) == Calendar.JANUARY
                && (c.get(Calendar.DAY_OF_MONTH) == 29 || c.get(Calendar.DAY_OF_MONTH) == 30 || c.get(Calendar.DAY_OF_MONTH) == 31))
                && months == 1) {

            period.setEndDate(DateUtils.incDays(period.getIniDate(), 30));
            return;
        }

        Date dt = DateUtils.incMonths(period.getIniDate(), months);
        dt = DateUtils.incDays(dt, -1);

        period.setEndDate(dt);
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
