package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.fields.PersonNameField;
import org.msh.etbm.commons.models.db.DBFieldsDef;
import org.msh.etbm.commons.models.impl.FieldContext;
import org.msh.etbm.db.PersonName;

import java.util.HashMap;
import java.util.Map;

/**
 * Field handler to support operations with the {@link PersonNameField}
 *
 * Created by rmemoria on 26/8/16.
 */
public class PersonNameFieldHandler extends FieldHandler<PersonNameField> {

    @Override
    protected Object convertValue(PersonNameField field, FieldContext fieldContext, Object value) {
        if (value == null) {
            return value;
        }

        if (value instanceof PersonName) {
            return value;
        }

        if (!(value instanceof Map)) {
            throw new ModelException("Invalid value for person name: " + value);
        }

        Map<String, String> map = (Map)value;

        PersonName person = new PersonName();

        person.setName(map.get(field.getFieldName()));
        person.setMiddleName(map.get(field.getFieldMiddleName()));
        person.setLastName(map.get(field.getFieldLastName()));

        return person;
    }

    @Override
    protected void validateValue(PersonNameField field, FieldContext context, Object value) {

    }

    @Override
    public Map<String, Object> mapFieldsToSave(PersonNameField field, Object value) {
        PersonName p = (PersonName)value;
        Map<String, Object> fields = new HashMap<>();

        fields.put( field.getFieldName(), p.getName() );
        fields.put( field.getFieldMiddleName(), p.getMiddleName() );
        fields.put( field.getFieldLastName(), p.getLastName() );

        return fields;
    }

    @Override
    public void dbFieldsToSelect(PersonNameField field, DBFieldsDef defs, boolean displaying) {
        defs.add(field.getFieldName())
            .add(field.getFieldMiddleName())
            .add(field.getFieldLastName());
    }

    @Override
    public Object readMultipleValuesFromDb(PersonNameField field, Map<String, Object> values, boolean displaying) {
        PersonName p = new PersonName();

        p.setName( (String)values.get(field.getFieldName()) );
        p.setMiddleName( (String)values.get(field.getFieldMiddleName()) );
        p.setLastName( (String) values.get(field.getFieldLastName()) );

        return p;
    }
}
