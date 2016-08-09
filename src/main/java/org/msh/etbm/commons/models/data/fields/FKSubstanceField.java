package org.msh.etbm.commons.models.data.fields;

/**
 * Created by rmemoria on 11/7/16.
 */
@FieldType("substance")
public class FKSubstanceField extends AbstractForeignKeyField {

    @Override
    public String getForeignTable() {
        return "substance";
    }

    @Override
    public String getForeignDisplayingFieldName() {
        return "name";
    }
}
