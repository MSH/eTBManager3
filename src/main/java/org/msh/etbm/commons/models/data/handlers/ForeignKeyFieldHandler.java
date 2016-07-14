package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.models.data.fields.ForeignKeyField;
import org.msh.etbm.commons.models.db.DBFieldsDef;
import org.msh.etbm.commons.models.impl.FieldContext;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 11/7/16.
 */
public class ForeignKeyFieldHandler<E extends ForeignKeyField> extends SingleFieldHandler<E> {

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
        return value;
    }

    @Override
    protected void validateValue(E field, FieldContext context, Object value) {
        // TODO
    }

    @Override
    public Map<String, Object> mapFieldsToSave(E field, Object value) {
        return Collections.singletonMap(field.getDbFieldName(), ObjectUtils.uuidAsBytes((UUID)value));
    }

    @Override
    public void dbFieldsToSelect(E field, DBFieldsDef defs, boolean displaying) {
        defs.add(field.getDbFieldName());
        if (displaying) {
            defs.join(field.getForeignTable(),
                    field.getForeignTable() + "." + field.getForeignTableKeyName() + " = " +
                            defs.getRootTable() + "." + field.getDbFieldName())
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
    public Object readMultipleValuesFromDb(E field, Map<String, Object> values) {
        byte[] data = (byte[])values.get(field.getDbFieldName());
        if (data == null) {
            return null;
        }
        UUID id = ObjectUtils.bytesToUUID(data);
        Object label = values.get(field.getForeignDisplayingFieldName());
        return new Item<>(id, label.toString());
    }
}
