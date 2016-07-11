package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.data.fields.SingleField;
import org.msh.etbm.commons.models.data.fields.StringField;
import org.msh.etbm.commons.models.db.DBFieldsDef;

import java.util.HashMap;
import java.util.Map;

/**
 * Field handler that supports one single DB field to store data
 * Created by rmemoria on 7/7/16.
 */
public abstract class SingleFieldHandler<E extends SingleField> extends FieldHandler<E> {

    @Override
    public Map<String, Object> mapFieldsToSave(E field, Object value) {
        Map<String, Object> map = new HashMap<>();

        // get the name of the field to map
        String dbfield = field.getDbFieldName();
        if (dbfield == null) {
            dbfield = field.getName();
        }

        map.put(dbfield, value);
        return map;
    }

    @Override
    public void dbFieldsToSelect(SingleField field, DBFieldsDef defs, boolean displaying) {
        String s = field.getDbFieldName() != null ? field.getDbFieldName() : field.getName();
        defs.add(s);
    }
}
