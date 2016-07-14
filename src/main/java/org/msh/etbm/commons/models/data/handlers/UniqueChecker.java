package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.Messages;
import org.msh.etbm.commons.models.FieldTypeManager;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.impl.FieldContext;
import org.msh.etbm.commons.models.impl.ModelResources;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Check if the field value is unique in the database. Called when field property 'unique' is true
 *
 * Created by rmemoria on 14/7/16.
 */
public class UniqueChecker {

    /**
     * Check if field value is unique
     * @param context the field context, with information about model and error messages
     * @param value the value to check
     * @param resources available resources, in this case, the data source to query the database
     * @return true if the value is unique, otherwise return false and add an error for this field
     */
    public static boolean checkUnique(FieldContext context, Object value, ModelResources resources) {
        // is unique rule ?
        Field field = context.getField();
        if (!field.isUnique() || value == null) {
            return true;
        }

        // check if resource is available
        if (resources == null) {
            throw new ModelException("Cannot test unique value. No resource available");
        }

        Model model = context.getParent().getModel();

        StringBuilder s = new StringBuilder();
        s.append("select count(*) from ")
                .append(model.resolveTableName())
                .append(" where workspace_id = ?");

        // create the list of parameters
        List params = new ArrayList<>();
        params.add(ObjectUtils.uuidAsBytes(resources.getWorkspaceId()));

        // get fields to use in query
        FieldHandler handler = FieldTypeManager.instance().getHandler(field.getTypeName());
        Map<String, Object> fields = handler.mapFieldsToSave(field, value);

        for (Map.Entry<String, Object> entry: fields.entrySet()) {
            s.append(" and ").append(entry.getKey()).append(" = ?");
            params.add(entry.getValue());
        }

        // check if it is an existing record
        UUID id = context.getParent().getId();
        if (id != null) {
            s.append(" and id <> ?");
            params.add(ObjectUtils.uuidAsBytes(id));
        }

        return execQuery(s.toString(), context, resources.getDataSource(), params);
    }


    private static boolean execQuery(String sql, FieldContext context, DataSource dataSource, List params) {
        JdbcTemplate templ = new JdbcTemplate(dataSource);

        Integer count = templ.queryForObject(sql, params.toArray(), Integer.class);

        // check if value is not unique
        if (count != null && count > 0) {
            context.getParent().getErrors().rejectValue(context.getField().getName(), Messages.NOT_UNIQUE);
            return false;
        }

        return true;
    }
}
