package org.msh.etbm.commons.models;

import org.springframework.validation.Errors;

import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 15/7/16.
 */
public interface ModelDAOInterceptor {

    void beforeValidate(UUID id, Map<String, Object> values, Errors errors);

    void beforeSave(UUID id, Map<String, Object> values, Errors errors);

    void afterSave(UUID id, Map<String, Object> values);

    void beforeDelete(UUID id, Map<String, Object> values);

    void afterDelete(UUID id, Map<String, Object> values);
}
