package org.msh.etbm.commons.models.data.fields;

/**
 * Created by rmemoria on 12/7/16.
 */
@FieldType("unit")
public class FKUnitField extends AbstractForeignKeyField {

    /**
     * If true, in displaying mode, it will include information about administrative unit of the unit
     */
    private boolean includeAdminUnit;

    public FKUnitField() {
        super();
    }

    public FKUnitField(String name) {
        super(name);
    }

    public FKUnitField(String name, String dbFieldName) {
        super(name, dbFieldName);
    }

    @Override
    public String getForeignTable() {
        return "unit";
    }

    @Override
    public String getForeignDisplayingFieldName() {
        return "name";
    }

    public boolean isIncludeAdminUnit() {
        return includeAdminUnit;
    }

    public void setIncludeAdminUnit(boolean includeAdminUnit) {
        this.includeAdminUnit = includeAdminUnit;
    }
}
