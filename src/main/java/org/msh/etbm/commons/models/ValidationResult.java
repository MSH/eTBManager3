package org.msh.etbm.commons.models;

import org.springframework.validation.Errors;

import java.util.Map;

/**
 * Contains the result of a validation operation. A validation operation may change the content of
 * the values validated, and also generate a list of values
 *
 * Created by rmemoria on 6/7/16.
 */
public class ValidationResult {

    private Errors errors;
    private Map<String, Object> values;

    protected ValidationResult(Errors errors, Map<String, Object> values) {
        this.errors = errors;
        this.values = values;
    }

    public Errors getErrors() {
        return errors;
    }

    public Map<String, Object> getValues() {
        return values;
    }
}
