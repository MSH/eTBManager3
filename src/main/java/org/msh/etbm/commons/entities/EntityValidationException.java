package org.msh.etbm.commons.entities;

import org.springframework.validation.BindingResult;

/**
 * Exception generated inside an entity to indicate that an error occured and operation
 * cannot continue
 *
 * Created by rmemoria on 28/10/15.
 */
public class EntityValidationException extends RuntimeException {
    private transient final BindingResult bindingResult;

    private final String field;
    private final String code;

    /**
     * Constructor when there is just one single validation error message
     * @param field
     * @param message
     * @param code
     */
    public EntityValidationException(String field, String message, String code) {
        super(message);
        this.field = field;
        this.code = code;
        this.bindingResult = null;
    }

    /**
     * Constructor when validation messages are already in the binding result object
     * @param bindingResult instance of BindingResult object containing validation messages
     */
    public EntityValidationException(BindingResult bindingResult) {
        super();
        this.bindingResult = bindingResult;
        this.field = null;
        this.code = null;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public String getField() {
        return field;
    }

    public String getCode() {
        return code;
    }


}
