package org.msh.etbm.commons.forms;

import org.msh.etbm.commons.forms.FieldInitRequest;
import org.msh.etbm.commons.forms.FieldInitResponse;

import java.util.List;
import java.util.UUID;

/**
 * Form request data to be sent by the client
 * Created by ricardo on 21/01/16.
 */
public class FormRequest {
    private UUID id;
    private String formId;
    private List<FieldInitRequest> fields;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public List<FieldInitRequest> getFields() {
        return fields;
    }

    public void setFields(List<FieldInitRequest> fields) {
        this.fields = fields;
    }
}
