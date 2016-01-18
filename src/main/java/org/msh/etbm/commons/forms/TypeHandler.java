package org.msh.etbm.commons.forms;

/**
 * Declare an interface for objects that wants to handle field initialization
 * with custom types
 *
 * Created by rmemoria on 17/1/16.
 */
public interface TypeHandler<R> {

    /**
     * Return the name to be assigned to the type
     * @return name of the type
     */
    String getTypeName();

    /**
     * Initialize the field based on the request params
     * @param req
     * @return
     */
    R initField(FieldInitRequest req);
}
