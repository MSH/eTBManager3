package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.models.data.TableColumn;
import org.msh.etbm.commons.models.data.TableColumnType;
import org.msh.etbm.commons.models.data.fields.AbstractForeignKeyField;
import org.msh.etbm.commons.models.db.DBFieldsDef;
import org.msh.etbm.commons.models.impl.FieldContext;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.util.*;

/**
 * Created by rmemoria on 11/7/16.
 */
public class ForeignKeyFieldHandler<E extends AbstractForeignKeyField> extends SingleFieldHandler<E> {

    private Class<E> fieldClass;

    public ForeignKeyFieldHandler(Class<E> fieldClass) {
        this.fieldClass = fieldClass;
    }

    @Override
    public Class<E> getFieldClass() {
        return fieldClass;
    }

    @Override
    protected Object convertValue(E field, FieldContext fieldContext, Object value) {

        if (value == null) {
            return null;
        }

        if (value instanceof UUID) {
            return value;
        }

        if (value instanceof String) {
            return UUID.fromString((String) value);
        }

        registerConversionError(fieldContext);
        return value;
    }

    @Override
    protected void validateValue(E field, FieldContext context, Object value) {
        // TODO
    }

    @Override
    public Map<String, Object> mapFieldsToSave(E field, Object value) {
        if (value == null) {
            return Collections.singletonMap(field.getFieldName(), null);
        }
        return Collections.singletonMap(field.getFieldName(), ObjectUtils.uuidAsBytes((UUID)value));
    }

    @Override
    public void dbFieldsToSelect(E field, DBFieldsDef defs, boolean displaying) {
        defs.add(field.getFieldName());
        if (displaying && field.getForeignDisplayingFieldName() != null) {
            defs.join(field.getForeignTable(),
                    field.getForeignTable() + "." + field.getForeignTableKeyName() + " = " +
                            defs.getRootTable() + "." + field.getFieldName())
                    .add(field.getForeignDisplayingFieldName());
        }
    }

    @Override
    public Object readSingleValueFromDb(E field, Object value) {
        if (value == null) {
            return null;
        }
        UUID id = ObjectUtils.bytesToUUID((byte[]) value);
        return id;
    }

    @Override
    public Object readMultipleValuesFromDb(E field, Map<String, Object> values, boolean displaying) {
        byte[] data = (byte[])values.get(field.getFieldName());
        if (data == null) {
            return null;
        }
        UUID id = ObjectUtils.bytesToUUID(data);
        Object label = values.get(field.getForeignDisplayingFieldName());
        return new Item<>(id, label.toString());
    }

    @Override
    public List<TableColumn> getTableFields(E field) {
        List<TableColumn> lst = new ArrayList<>();

        lst.add(new TableColumn(getFieldName(field), TableColumnType.BINARY, 16));
        return lst;
    }
}
