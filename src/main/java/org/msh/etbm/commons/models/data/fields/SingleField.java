package org.msh.etbm.commons.models.data.fields;

/**
 * Created by rmemoria on 7/7/16.
 */
public abstract class SingleField extends Field {

    /**
     * Name of the field in the database table
     */
    private String dbFieldName;

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }
}
