package org.msh.etbm.commons.models.data.fields;

/**
 * Created by rmemoria on 11/7/16.
 */
@FieldType("regimen")
public class FKRegimenField extends AbstractForeignKeyField {
    @Override
    public String getForeignTable() {
        return "regimen";
    }

    @Override
    public String getForeignDisplayingFieldName() {
        return "name";
    }
}
