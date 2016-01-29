package org.msh.etbm.commons.entities.cmdlog;

import org.msh.etbm.commons.Displayable;
import org.msh.etbm.commons.entities.ObjectValues;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Utilities to generate log values of an object based on its property log information and the operation
 * under progress
 *
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
     * @param vals
     * @param obj
     * @param clazz
     * @param oper
     */
    protected void generateLogValues(ObjectValues vals, Object obj, Class clazz, Operation oper) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field: fields) {
            analyseProperty(vals, obj, field, oper);
        }
    }

    /**
     * Return true if the field must be logged
     * @param field
     * @param oper
     * @return
     */
    protected boolean isFieldLogged(Field field, Operation oper) {
        PropertyLog propertyLog = field.getAnnotation(PropertyLog.class);

        if (propertyLog != null) {
            if (propertyLog.ignore()) {
                return false;
            }
            Operation[] opers = propertyLog.operations();

            // if operation is not supported, exit
            if ((Arrays.binarySearch(opers, oper) == -1) && (Arrays.binarySearch(opers, Operation.ALL) == -1)) {
                return false;
            }
        }
        else
            // the default operation is EDIT or type is a collection ?
            if (oper != Operation.EDIT || classImplementsInterface(field.getType(), Collection.class)) {
                return false;
            }

        return true;
    }


    /**
     * Return the message key to log the field value
     * @param field
     * @return
     */
    protected String getMessageKey(Field field) {
        PropertyLog log = field.getAnnotation(PropertyLog.class);
        if (log == null || log.messageKey().isEmpty()) {
            if (!field.getType().isPrimitive()) {
                return field.getDeclaringClass().getSimpleName() + "." + field.getName();
            }
            else {
                return field.getType().getSimpleName();
            }
        }
        else {
            return log.messageKey();
        }
    }

    /**
     * Analyse property for logging
     * @param vals
     * @param field
     * @param oper
     */
    protected void analyseProperty(ObjectValues vals, Object obj, Field field, Operation oper) {
        // get annotation
        if (!isFieldLogged(field, oper)) {
            return;
        }

        Object val = ObjectUtils.getProperty(obj, field.getName());

        if (val == null) {
            return;
        }

        if (!val.getClass().isPrimitive() && !(val instanceof Date)) {
            if (val instanceof Displayable) {
                val = ((Displayable)val).getDisplayString();
            }
            else {
                val = val.toString();
            }
        }

        String key = getMessageKey(field);

        vals.put(key, val);
    }

    /**
     * Check if class implements a particular interface
     * @param clazz
     * @return
     */
    private boolean classImplementsInterface(Class clazz, Class interf) {
        if (clazz.isInterface())
            return isInterfaceImplemented(clazz, interf);

        if (clazz.isPrimitive())
            return false;

        Class[] ints = clazz.getInterfaces();

        if (ints != null) {
            for (Class intf: clazz.getInterfaces()) {
                if (isInterfaceImplemented(intf, interf))
                    return true;
            }
        }

        Class parent = clazz.getSuperclass();
        if (parent == Object.class)
            return false;

        return classImplementsInterface(parent, interf);
    }


    /**
     * Check if a given interface extends another interface
     * @param clazz
     * @param interf
     * @return
     */
    private boolean isInterfaceImplemented(Class clazz, Class interf) {
        if (clazz == interf)
            return true;

        Class[] lst = clazz.getInterfaces();
        for (Class aux: lst)
            if (isInterfaceImplemented(aux, interf))
                return true;

        return false;
    }

}
