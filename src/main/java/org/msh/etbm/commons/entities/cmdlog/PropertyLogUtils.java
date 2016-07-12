package org.msh.etbm.commons.entities.cmdlog;

import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.objutils.ObjectValues;
import org.msh.etbm.commons.objutils.PropertyValue;
import org.msh.etbm.db.Synchronizable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;

/**
 * Utilities to generate log values of an object based on its property log information and the operation
 * under progress
 * <p>
 * Created by rmemoria on 26/10/15.
 */
public class PropertyLogUtils {

    public static ObjectValues generateLog(Object obj, Class clazz, Operation oper) {
        PropertyLogUtils pl = new PropertyLogUtils();

        ObjectValues ov = new ObjectValues();

        pl.generateLogValues(ov, obj, clazz, oper);

        return ov;
    }

    /**
     * Generate the log values for the object
     *
     * @param vals
     * @param obj
     * @param clazz
     * @param oper
     */
    protected void generateLogValues(ObjectValues vals, Object obj, Class clazz, Operation oper) {
        Class objClass = clazz;

        while (objClass != Object.class && objClass != null) {
            Field[] fields = objClass.getDeclaredFields();

            for (Field field : fields) {
                analyseProperty(vals, obj, field, oper);
            }

            objClass = objClass.getSuperclass();
        }
    }

    /**
     * Return true if the field must be logged
     *
     * @param field the field to be analysed
     * @param oper  the log operation (new, edit or delete)
     * @return true if the field must be logged, otherwise false if the field should not be logged
     */
    protected boolean isFieldLogged(Field field, Operation oper) {
        if (Modifier.isStatic(field.getModifiers())) {
            return false;
        }

        PropertyLog propertyLog = field.getAnnotation(PropertyLog.class);

        if (propertyLog != null) {
            if (propertyLog.ignore()) {
                return false;
            }
            Operation[] opers = propertyLog.operations();

            // just to make testing easier
            if (oper == Operation.ALL) {
                return true;
            }

            // if operation is not supported, exit
            if ((Arrays.binarySearch(opers, oper) == -1) && (Arrays.binarySearch(opers, Operation.ALL) == -1)) {
                return false;
            }
        } else if (oper != Operation.EDIT && oper != Operation.ALL) {
            // if is not an edit operation (or a log to return all items), so the field shall not be logged
            return false;
        }

        return true;
    }


    /**
     * Return the message key to log the field value
     *
     * @param field
     * @return
     */
    protected String getMessageKey(Field field) {
        PropertyLog log = field.getAnnotation(PropertyLog.class);
        if (log == null || log.messageKey().isEmpty()) {

            // is an enum type ?
            if (field.getType().isEnum()) {
                // so return the enum class name
                return field.getType().getSimpleName();
            }

            // is an entity class from the system ?
            if (Synchronizable.class.isAssignableFrom(field.getType())) {
                return field.getType().getSimpleName();
            }

            // is a primitive type ?
            return field.getDeclaringClass().getSimpleName() + "." + field.getName();
        } else {
            return log.messageKey();
        }
    }

    /**
     * Analyse property for logging
     *
     * @param vals
     * @param field
     * @param oper
     */
    protected void analyseProperty(ObjectValues vals, Object obj, Field field, Operation oper) {
        // get annotation
        if (!isFieldLogged(field, oper)) {
            return;
        }

        // check how log is done (whole object or its properties)
        PropertyLog log = field.getAnnotation(PropertyLog.class);

        // get key ready to be formatted
        String key = "$" + getMessageKey(field);

        // get single property of the object
        Object val = ObjectUtils.getProperty(obj, field.getName());

        // must add properties of the field object ?
        if (val != null && log != null && log.addProperties() && !field.getType().isPrimitive()) {
            // scan properties of the linked object
            ObjectValues fieldVals = generateLog(val, field.getType(), oper);

            for (Map.Entry<String, PropertyValue> entry : fieldVals.getValues().entrySet()) {
                vals.putPropertyValue(key + " - " + entry.getKey(), entry.getValue());
            }
        } else {
            vals.put(key, val);
        }
    }

    /**
     * Check if class implements a particular interface
     *
     * @param clazz
     * @return
     */
    private boolean classImplementsInterface(Class clazz, Class interf) {
        if (clazz.isInterface()) {
            return isInterfaceImplemented(clazz, interf);
        }

        if (clazz.isPrimitive()) {
            return false;
        }

        Class[] ints = clazz.getInterfaces();

        if (ints != null) {
            for (Class intf : clazz.getInterfaces()) {
                if (isInterfaceImplemented(intf, interf)) {
                    return true;
                }
            }
        }

        Class parent = clazz.getSuperclass();
        if (parent == Object.class) {
            return false;
        }

        return classImplementsInterface(parent, interf);
    }


    /**
     * Check if a given interface extends another interface
     *
     * @param clazz
     * @param interf
     * @return
     */
    private boolean isInterfaceImplemented(Class clazz, Class interf) {
        if (clazz == interf) {
            return true;
        }

        Class[] lst = clazz.getInterfaces();
        for (Class aux : lst) {
            if (isInterfaceImplemented(aux, interf)) {
                return true;
            }
        }

        return false;
    }

}
