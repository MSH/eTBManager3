package org.msh.etbm.commons.models;

import org.springframework.validation.Errors;

import java.util.UUID;

/**
 * Created by rmemoria on 13/7/16.
 */
public class ModelDAOResult {

    private UUID id;
    private Errors errors;

    public ModelDAOResult(UUID id, Errors errors) {
        this.id = id;
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }

    public UUID getId() {
        return id;
    }
}
