package org.msh.etbm.services.admin.units;

/**
 * Indicate classes that hold information about the unit type.
 *
 * Usually implemented to make it easier to map from Unit class to other classes
 *
 * Created by rmemoria on 1/11/15.
 */
public interface TypedUnit {
    /**
     * Return the unit type (laboratory or Tbunit)
     * @return
     */
    UnitType getType();

    /**
     * Set the type of unit
     * @param type
     */
    void setType(UnitType type);
}
