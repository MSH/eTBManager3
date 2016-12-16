package org.msh.etbm.db.enums;

import org.msh.etbm.db.entities.*;

/**
 * Type of information stored as a searchable item
 */
public enum SearchableType {
    WORKSPACE(Workspace.class, null, "workspace"),
    ADMINUNIT(AdministrativeUnit.class, null, "administrativeunit"),
    TBUNIT(Tbunit.class, Unit.class, "unit"),
    LAB(Laboratory.class, Unit.class, "unit"),
    CASE_MAN(TbCase.class, null, "tbcase"),
    CASE_WOMAN(TbCase.class, null, "tbcase");

    SearchableType(Class entityClass, Class parentClass, String tableName) {
        this.entityClass = entityClass;
        this.parentClass = parentClass;
        this.tableName = tableName;
    }

    private Class entityClass;
    private Class parentClass;
    private String tableName;

    public Class getEntityClass() {
        return entityClass;
    }

    public Class getParentClass() {
        return parentClass;
    }

    public String getTableName() {
        return tableName;
    }

    public static final SearchableType findByTable(String tableName) {
        for (SearchableType s : SearchableType.values()) {
            if (s.getTableName().equals(tableName)) {
                return s;
            }
        }

        return null;
    }
}
