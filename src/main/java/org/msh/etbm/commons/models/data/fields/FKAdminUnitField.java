package org.msh.etbm.commons.models.data.fields;

/**
 * Created by rmemoria on 12/7/16.
 */
@FieldType("adminUnit")
public class FKAdminUnitField extends AbstractForeignKeyField {

    public FKAdminUnitField() {
        super();
    }

    public FKAdminUnitField(String name) {
        super(name);
    }

    public FKAdminUnitField(String name, String dbFieldName) {
        super(name, dbFieldName);
    }

    @Override
    public String getForeignTable() {
        return "administrativeunit";
    }

    @Override
    public String getForeignDisplayingFieldName() {
        return "name";
    }
}
