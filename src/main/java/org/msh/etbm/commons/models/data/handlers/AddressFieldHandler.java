package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.fields.AddressField;
import org.msh.etbm.commons.models.db.DBFieldsDef;
import org.msh.etbm.commons.models.impl.FieldContext;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.services.admin.AddressData;
import org.msh.etbm.services.admin.AddressEditData;
import org.msh.etbm.services.admin.admunits.data.AdminUnitData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 26/8/16.
 */
public class AddressFieldHandler extends FieldHandler<AddressField> {

    @Override
    protected Object convertValue(AddressField field, FieldContext fieldContext, Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof AddressEditData) {
            return value;
        }

        if (!(value instanceof Map)) {
            throw new ModelException("Invalid type for address: " + value.getClass());
        }

        AddressEditData addr = new AddressEditData();

        Map<String, String> map = (Map)value;
        addr.setAddress(map.get(field.getFieldAddress()));
        addr.setComplement(map.get(field.getFieldComplement()));
        addr.setZipCode(map.get(field.getFieldZipCode()));

        String auId = map.get(field.getFieldAdminUnit());
        UUID id = auId != null && !auId.isEmpty() ? UUID.fromString(auId) : null;
        addr.setAdminUnitId(id);
        return addr;
    }

    @Override
    protected void validateValue(AddressField field, FieldContext context, Object value) {

    }

    @Override
    public Map<String, Object> mapFieldsToSave(AddressField field, Object value) {
        AddressEditData addr = (AddressEditData)value;

        Map<String, Object> fields = new HashMap<>();

        fields.put(field.getFieldAddress(), addr.getAddress());
        fields.put(field.getFieldComplement(), addr.getComplement());
        fields.put(field.getFieldZipCode(), addr.getZipCode());
        fields.put(field.getFieldAdminUnit(), addr.getAdminUnitId());

        return fields;
    }

    /**
     * Convert an object value to an UUID type. The supported types are UUID,
     * array of bytes and string. Other types, a {@link ModelException} is throw
     * @param value
     * @return
     */
    protected UUID asUUID(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof UUID) {
            return (UUID)value;
        }

        if (value instanceof byte[]) {
            byte[] bytes = (byte[])value;
            return ObjectUtils.bytesToUUID(bytes);
        }

        throw new ModelException("Invalid type to converr to UUID: " + value);
    }

    protected SynchronizableItem getItem(Object id, Object name) {
        UUID valId = asUUID(id);
        if (valId == null) {
            return null;
        }

        return new SynchronizableItem(valId, (String)name);
    }

    @Override
    public Object readMultipleValuesFromDb(AddressField field, Map<String, Object> values, boolean displaying) {
        if (displaying) {
            AddressData addr = new AddressData();
            addr.setAddress((String)values.get(field.getFieldAddress()));
            addr.setComplement((String) values.get(field.getFieldComplement()) );
            addr.setZipCode( (String)values.get(field.getFieldZipCode()) );

            byte[] b = (byte[])values.get("id");
            UUID id = b != null && b.length > 0 ? ObjectUtils.bytesToUUID(b) : null;
            if (id != null) {
                AdminUnitData au = new AdminUnitData();
                au.setId(id);
                au.setP0(getItem(values.get("pid0"), values.get("pname0")));
                au.setP1(getItem(values.get("pid1"), values.get("pname1")));
                au.setP2(getItem(values.get("pid2"), values.get("pname2")));
                au.setP3(getItem(values.get("pid3"), values.get("pname3")));
            }
            return addr;
        }

        AddressEditData addr = new AddressEditData();
        addr.setAddress((String)values.get(field.getFieldAddress()));
        addr.setComplement((String) values.get(field.getFieldComplement()) );
        addr.setZipCode( (String)values.get(field.getFieldZipCode()) );
        addr.setAdminUnitId( asUUID(values.get(field.getFieldAdminUnit())));

        return addr;
    }

    @Override
    public void dbFieldsToSelect(AddressField field, DBFieldsDef defs, boolean displaying) {
        if (displaying) {
            defs.add(field.getFieldAddress())
                .add(field.getFieldComplement())
                .add(field.getFieldZipCode())
                .join("admininistrativeunit", "$this.id = $parent." + field.getFieldAdminUnit())
                .add("id")
                .add("pid0")
                .add("pid1")
                .add("pid2")
                .add("pid3")
                .add("name")
                .add("pname0")
                .add("pname1")
                .add("pname2")
                .add("pname3");
        } else {
            defs.add(field.getFieldAddress())
                .add(field.getFieldAdminUnit())
                .add(field.getFieldComplement())
                .add(field.getFieldZipCode());
        }
    }
}