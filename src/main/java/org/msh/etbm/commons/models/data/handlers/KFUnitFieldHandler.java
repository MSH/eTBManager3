package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.entities.EntityUtils;
import org.msh.etbm.commons.models.data.fields.FKUnitField;
import org.msh.etbm.commons.models.db.DBFieldsDef;
import org.msh.etbm.commons.models.impl.FieldContext;
import org.msh.etbm.services.admin.units.UnitType;
import org.msh.etbm.services.admin.units.data.UnitItemData;

import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 12/7/16.
 */
public class KFUnitFieldHandler extends SingleFieldHandler<FKUnitField> {

    @Override
    protected Object convertValue(FKUnitField field, FieldContext fieldContext, Object value) {
        return null;
    }

    @Override
    protected void validateValue(FKUnitField field, FieldContext context, Object value) {

    }

    @Override
    public Map<String, Object> mapFieldsToSave(FKUnitField field, Object value) {
        return null;
    }

    @Override
    public void dbFieldsToSelect(FKUnitField field, DBFieldsDef defs, boolean displaying) {
        // since the model can change from required to not required, it is wise to
        // keep join as left join
        defs.add(field.getDbFieldName())
                .leftJoin("unit", "$this.id = $parent." + field.getDbFieldName())
                .add("name")
                .add("discriminator");
    }

    @Override
    public Object readSingleValueFromDb(FKUnitField field, Object value) {
        if (value == null) {
            return null;
        }
        UUID id = EntityUtils.bytesToUUID((byte[])value);
        return id;
    }

    @Override
    public Object readMultipleValuesFromDb(FKUnitField field, Map<String, Object> values) {
        byte[] data = (byte[])values.get(field.getDbFieldName());
        if (data == null) {
            return null;
        }
        UUID id = EntityUtils.bytesToUUID(data);
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
