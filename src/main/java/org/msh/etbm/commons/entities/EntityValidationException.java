package org.msh.etbm.commons.entities;

import org.springframework.validation.BindingResult;

/**
 * Exception generated inside an entity to indicate that an error occured and operation
 * cannot continue
 *
 * Created by rmemoria on 28/10/15.
 */
public class EntityValidationException extends RuntimeException {
    private BindingResult bindingResult;

    private String field;
    private String message;
    private String code;

    public EntityValidationException(BindingResult res) {
        this.bindingResult = res;
    }

    public EntityValidationException(String field, String message, String code) {
        this.field = field;
        this.message = message;
        this.code = code;
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

    @Override
    public String getMessage() {
        return message;
    }
}
