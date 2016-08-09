package org.msh.etbm.commons.models.data.fields;

/**
 * Created by rmemoria on 8/8/16.
 */
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
