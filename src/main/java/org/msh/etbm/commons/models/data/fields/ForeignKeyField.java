package org.msh.etbm.commons.models.data.fields;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by rmemoria on 11/7/16.
 */
public abstract class ForeignKeyField extends SingleField {

    public ForeignKeyField() {
    }

    public ForeignKeyField(String name) {
        super(name);
    }

    public ForeignKeyField(String name, String dbFieldName) {
        super(name, dbFieldName);
    }

    @JsonIgnore
    public abstract String getForeignTable();

    @JsonIgnore
    public abstract String getForeignDisplayingFieldName();

    @JsonIgnore
    public String getForeignTableKeyName() {
        return "id";
    }


}
