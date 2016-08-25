package org.msh.etbm.commons.models.db;

import org.msh.etbm.commons.models.FieldTypeManager;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.handlers.FieldHandler;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.*;

/**
 * Load records from a table from the query provided by {@link SQLQuerySelectionBuilder}
 * Created by rmemoria on 11/7/16.
 */
public class SQLQueryLoader {

    /**
     * Execute the given query located in {@link SQLQueryInfo} and parse the results into
     * instances of {@link RecordData} classes
     * @param dataSource
     * @param sel
     * @return
     */
    public List<RecordData> loadData(DataSource dataSource, SQLQueryInfo sel) {
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);

        SqlParameterSource params = sel.getParameters() != null ?
                new MapSqlParameterSource(sel.getParameters()) :
                null;

        List<Map<String, Object>> lst = template.queryForList(sel.getSql(), params);
        return convertValues(lst, sel);
    }


    private List<RecordData> convertValues(List<Map<String, Object>> lst, SQLQueryInfo sel) {
        List<RecordData> res = new ArrayList<>();

        for (Map<String, Object> map: lst) {
            res.add(convertRecord(map, sel));
        }
        return res;
    }


    private RecordData convertRecord(Map<String, Object> map, SQLQueryInfo qry) {
        RecordData res = new RecordData();

        // load the ID
        byte[] val = (byte[])map.get("id");
        UUID id = ObjectUtils.bytesToUUID(val);
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
            res.getValues().put(field.getName(), newValue);
        }

        return res;
    }
}
