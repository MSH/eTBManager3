package org.msh.etbm.commons.forms.types;

import org.msh.etbm.commons.forms.FieldInitRequest;

/**
 * Declare an interface for objects that wants to handle field initialization
 * with custom types
 *
 * Created by rmemoria on 17/1/16.
 */
public interface TypeHandler<R> {

    /**
     * Initialize the field based on the request params
     * @param req
     * @return
     */
    R initField(FieldInitRequest req);
}
