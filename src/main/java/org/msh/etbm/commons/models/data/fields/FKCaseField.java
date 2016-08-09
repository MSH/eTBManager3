package org.msh.etbm.commons.models.data.fields;

/**
 * Created by rmemoria on 8/8/16.
 */
@FieldType("case")
public class FKCaseField extends AbstractForeignKeyField  {
    @Override
    public String getForeignTable() {
        return "tbcase";
    }

    @Override
    public String getForeignDisplayingFieldName() {
        return "diagnosisDate";
    }
}
