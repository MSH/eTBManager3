package org.msh.etbm.commons.models.tableupdate;

import org.msh.etbm.commons.models.FieldTypeManager;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.FieldHandler;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.TableColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * Compare two models (an old one and a new one) and return the new table schema
 * Created by rmemoria on 3/1/17.
 */
public class ModelSchemaComparator {


    /**
     * Compare the old model with the new model and return a list of the table fields schema
     * @param oldModel
     * @param newModel
     * @return
     */
    public static List<FieldSchema> compare(Model oldModel, Model newModel) {
        List<FieldSchema> res = new ArrayList<>();

        for (Field field: newModel.getFields()) {
            Field oldField = oldModel.findFieldById(field.getId());

            List<FieldSchema> lst = getFieldSchema(field, oldField);

            if (lst != null) {
                res.addAll(lst);
            }
        }

        return res;
    }

    /**
     * Return the list of table field schema for the new model field
     * @param newField
     * @param oldField
     * @return
     */
    private static List<FieldSchema> getFieldSchema(Field newField, Field oldField) {
        // is new field
        if (oldField == null) {
            List<FieldSchema> res = new ArrayList<>();

            List<TableColumn> tblFields = getTableFields(newField);
            for (TableColumn tf: tblFields) {
                res.add(new FieldSchema(newField, null, tf));
            }

            return res;
        }

        List<TableColumn> oldTblFields = getTableFields(oldField);
        List<TableColumn> newTblFields = getTableFields(newField);

        List<FieldSchema> res = compareFields(newField, oldTblFields, newTblFields);
        return res;
    }


    /**
     * Compare the fields from previous version to the new version
     * @param field The new model field
     * @param oldFields the old table fields
     * @param newFields the new table fields
     * @return
     */
    private static List<FieldSchema> compareFields(Field field, List<TableColumn> oldFields, List<TableColumn> newFields) {
        if (oldFields.size() != newFields.size()) {
            throw new ModelException("Invalid number of fields from previous to new field");
        }

        List<FieldSchema> res = new ArrayList<>();

        for (int i = 0; i < newFields.size(); i++) {
            TableColumn oldf = oldFields.get(i);
            TableColumn newf = newFields.get(i);

            String newFieldName = newf.getName().equalsIgnoreCase(oldf.getName()) ? null : newf.getName();

            if (!oldf.equalsTo(newf) || newFieldName != null) {
                FieldSchema fs = new FieldSchema(field, newFieldName, newf);
                res.add(fs);
            }
        }

        return res.isEmpty() ? null : res;
    }

    private static List<TableColumn> getTableFields(Field field) {
        FieldHandler handler = FieldTypeManager.instance().getHandler(field.getTypeName());
        List<TableColumn> lst = handler.getTableFields(field);
        return lst;
    }
}
