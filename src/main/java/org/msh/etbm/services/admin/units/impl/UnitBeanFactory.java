package org.msh.etbm.services.admin.units.impl;

import org.dozer.BeanFactory;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.Laboratory;
import org.msh.etbm.db.entities.Tbunit;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.services.admin.units.TypedUnit;
import org.msh.etbm.services.admin.units.UnitType;

/**
 * Dozer bean factory to create instances of Units based on the data type and vice versa.
 *
 * If the target (or source) class implements the interface {@link TypedUnit}, the unit type
 * is set to the object instance
 *
 * Created by rmemoria on 1/11/15.
 */
public class UnitBeanFactory implements BeanFactory {

    @Override
    public Object createBean(Object obj, Class<?> aClass, String targetId) {
        if (obj instanceof Unit) {
            return createFromUnit((Unit)obj, aClass);
        }

        return createUnit(obj);
    }

    /**
     * Create target class with information from unit object. If target implements
     * {@link TypedUnit}, the type of unit will be automatically set
     * @param unit the instance of the Unit class
     * @param target the target class
     * @return new instnace of target
     */
    protected Object createFromUnit(Unit unit, Class target) {
        Object data = ObjectUtils.newInstance( target );

        if (data instanceof TypedUnit) {
            ((TypedUnit) data).setUnitType(unit.getType());
        }

        return data;
    }

    /**
     * Create instance of Unit based on given object
     * @param source object that must implement {@link TypedUnit} interface
     * @return instance of the Unit class
     */
    protected Unit createUnit(Object source) {
        if (!(source instanceof TypedUnit)) {
            throw new EntityValidationException("type",
                    "Source object must implement interface " + TypedUnit.class.getSimpleName() + "  --> " + source.getClass(),
                    null);
        }
        UnitType type = ((TypedUnit) source).getUnitType();

        switch (type) {
            case LAB: return new Laboratory();
            case TBUNIT: return new Tbunit();
        }

        throw new EntityValidationException("type", "Type not supported " + type, null);
    }
}
