package org.msh.etbm.commons.models.db;

import org.msh.etbm.commons.entities.EntityUtils;
import org.msh.etbm.commons.models.FieldTypeManager;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.handlers.FieldHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.*;

/**
 * Created by rmemoria on 11/7/16.
 */
public class DataLoader {

    public List<RecordDataMap> loadData(DataSource dataSource, SQLQueryInfo sel) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);

        SqlParameterSource params = sel.getParameters() != null ?
                new MapSqlParameterSource(sel.getParameters()) :
                null;

        List<Map<String, Object>> lst = template.queryForList(sel.getSql(), params);
        return convertValues(lst, sel);
    }


    private List<RecordDataMap> convertValues(List<Map<String, Object>> lst, SQLQueryInfo sel) {
        List<RecordDataMap> res = new ArrayList<>();

        for (Map<String, Object> map: lst) {
            res.add(convertRecord(map, sel));
        }
        return res;
    }


    private RecordDataMap convertRecord(Map<String, Object> map, SQLQueryInfo qry) {
        RecordDataMap res = new RecordDataMap();

        // load the ID
        byte[] val = (byte[])map.get("id");
        UUID id = EntityUtils.bytesToUUID(val);
        res.setId(id);

        // feed the values using the fields
        for (Map.Entry<Field, List<SQLQueryField>> entry: qry.getFields().entrySet()) {
            Field field = entry.getKey();
            List<SQLQueryField> lst = entry.getValue();

            FieldHandler handler = FieldTypeManager.instance().getHandler(field.getTypeName());

            Object newValue;
            if (lst.size() == 1) {
                SQLQueryField qryField = lst.get(0);
                Object value = map.get(qryField.getFieldNameAlias());
                newValue = handler.readSingleValueFromDb(field, value);
            } else {
                Map<String, Object> vals = new HashMap<>();
                for (SQLQueryField qryField: lst) {
                    vals.put(qryField.getFieldName(), map.get(qryField.getFieldNameAlias()));
                }
                newValue = handler.readMultipleValuesFromDb(field, vals);
            }
            res.put(field.getName(), newValue);
        }

        return res;
    }
}
