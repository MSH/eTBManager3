package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.data.fields.FKUnitField;
import org.msh.etbm.commons.models.db.DBFieldsDef;
import org.msh.etbm.commons.models.impl.FieldContext;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.services.admin.units.UnitType;
import org.msh.etbm.services.admin.units.data.UnitItemData;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 12/7/16.
 */
public class FKUnitFieldHandler extends SingleFieldHandler<FKUnitField> {

    @Override
    protected Object convertValue(FKUnitField field, FieldContext fieldContext, Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return UUID.fromString((String) value);
        }

        if (value instanceof UnitItemData) {
            return ((UnitItemData)value).getId();
        }

        if (value instanceof UUID) {
            return value;
        }

        throw new RuntimeException("Unit id value must be a String or UUID");
    }

    @Override
    protected void validateValue(FKUnitField field, FieldContext context, Object value) {

    }

    @Override
    public Map<String, Object> mapFieldsToSave(FKUnitField field, Object value) {
        return Collections.singletonMap(field.getFieldName(), ObjectUtils.uuidAsBytes((UUID)value));
    }

    @Override
    public void dbFieldsToSelect(FKUnitField field, DBFieldsDef defs, boolean displaying) {
        // since the model can change from required to not required, it is wise to
        // keep join as left join
        defs.add(field.getFieldName())
                .leftJoin("unit", "$this.id = $parent." + field.getFieldName())
                .add("name")
                .add("discriminator");
    }

    @Override
    public Object readSingleValueFromDb(FKUnitField field, Object value) {
        if (value == null) {
            return null;
        }
        UUID id = ObjectUtils.bytesToUUID((byte[]) value);
        return id;
    }

    @Override
    public Object readMultipleValuesFromDb(FKUnitField field, Map<String, Object> values, boolean displaying) {
        byte[] data = (byte[])values.get(field.getFieldName());
        if (data == null) {
            return null;
        }
        UUID id = ObjectUtils.bytesToUUID(data);
        String label = (String)values.get("name");
        String discriminator = (String)values.get("discriminator");

        UnitItemData unit = new UnitItemData();
        unit.setName(label);
        unit.setId(id);

        if ("lab".equals(discriminator)) {
            unit.setType(UnitType.LAB);
        } else if ("unit".equals(discriminator)) {
            unit.setType(UnitType.TBUNIT);
        }

        return unit;
    }

    
}
