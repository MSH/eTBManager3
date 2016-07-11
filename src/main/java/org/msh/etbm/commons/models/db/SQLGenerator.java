package org.msh.etbm.commons.models.db;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.msh.etbm.commons.models.FieldTypeManager;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.handlers.FieldHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 7/7/16.
 */
public class SQLGenerator {

    /**
     * Generate INSERT SQL statement
     * @param model the model to generate SQL from
     * @param doc
     * @return
     */
    public QueryData createInsertSQL(Model model, Map<String, Object> doc) {
        Map<String, Object> dbfields = mapDbFields(model, doc);

        TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();
        UUID id = uuidGenerator.generate();
        dbfields.put("id", id);

        StringBuilder s = new StringBuilder();

        String table = model.resolveTableName();

        s.append("insert into " + table + " (");

        // generate the list of fields
        String delim = "";
        for (String fieldname: dbfields.keySet()) {
            s.append(delim).append(fieldname);
            delim = ", ";
        }

        // generate the list of parameters
        s.append(") values (");
        delim = "";
        for (String fieldname: dbfields.keySet()) {
            s.append(delim).append(":").append(fieldname);
            delim = ", ";
        }
        s.append(")");

        return new QueryData(s.toString(), dbfields);
    }


    public QueryData createUpdateSQL(Model model, Map<String, Object> doc, UUID id) {
        Map<String, Object> dbfields = mapDbFields(model, doc);

        StringBuilder s = new StringBuilder();

        String table = model.resolveTableName();

        s.append("update ").append(table).append(" set ");

        String delim = "";
        for (String fname: dbfields.keySet()) {
            s.append(delim)
                    .append(fname)
                    .append(" = :")
                    .append(fname);
            delim = ", ";
        }

        s.append(" where id = :id");

        dbfields.put("id", id);

        return new QueryData(s.toString(), dbfields);
    }

    protected Map<String, Object> mapDbFields(Model model, Map<String, Object> doc) {
        Map<String, Object> res = new HashMap<>();

        for (Field field: model.getFields()) {
            FieldHandler handler = FieldTypeManager.instance().getHandler(field.getTypeName());
            Object value = doc.get(field.getName());

            Map<String, Object> dbfields = handler.mapFieldsToSave(field, value);

            res.putAll(dbfields);
        }

        return res;
    }
}
