package org.msh.etbm.commons.models.data.fields;

/**
 * Created by rmemoria on 8/8/16.
 */
public class ForeignKeyField extends AbstractForeignKeyField {

    private String fkTable;

    private String fkDisplayField;

    @Override
    public String getForeignTable() {
        return fkTable;
    }

    @Override
    public String getForeignDisplayingFieldName() {
        return fkDisplayField;
    }

    public String getFkTable() {
        return fkTable;
    }

    public void setFkTable(String fkTable) {
        this.fkTable = fkTable;
    }

    public String getFkDisplayField() {
        return fkDisplayField;
    }

    public void setFkDisplayField(String fkDisplayField) {
        this.fkDisplayField = fkDisplayField;
    }
}
