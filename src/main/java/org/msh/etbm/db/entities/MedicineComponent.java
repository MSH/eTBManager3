package org.msh.etbm.db.entities;

import org.msh.etbm.db.Synchronizable;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * Component of a medicine,
 * example: Isoniazid is a common component of medicines
 *
 * @author Ricardo Memoria
 */
//@Entity
//@Table(name = "medicinecomponent")
public class MedicineComponent extends Synchronizable {


    @ManyToOne
    @JoinColumn(name = "SUBSTANCE_ID")
    private Substance substance;

    private Integer strength;

    @ManyToOne
    @JoinColumn(name = "MEDICINE_ID")
    private Medicine medicine;


    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }


    public Substance getSubstance() {
        return substance;
    }

    public void setSubstance(Substance substance) {
        this.substance = substance;
    }

    public Integer getStrength() {
        return strength;
    }

    public void setStrength(Integer strength) {
        this.strength = strength;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ((substance != null) && (medicine != null) ? substance.getShortName() + " " + strength + medicine.toString() : super.toString());
    }


}
