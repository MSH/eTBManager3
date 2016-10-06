package org.msh.etbm.db.enums;

/**
 * Type of information stored as a searchable item
 */
public enum SearchableType {
    WORKSPACE("Workspace", null),
    ADMINUNIT("AdministrativeUnit", null),
    TBUNIT("Tbunit", "Unit"),
    LAB("Laboratory", "Unit"),
    CASE_MAN("TbCase", null),
    CASE_WOMAN("TbCase", null);

    SearchableType(String entityClassName, String parentClassName) {
        this.entityClassName = entityClassName;
        this.parentClassName = parentClassName;
    }

    private String entityClassName;
    private String parentClassName;

    public String getEntityClassName() {
        return entityClassName;
    }

    public String getParentClassName() {
        return parentClassName;
    }
}
