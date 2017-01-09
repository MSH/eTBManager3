package org.msh.etbm.commons.models.tableupdate;

import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Responsible for updating a table schema based on the changes in a model
 * Created by rmemoria on 3/1/17.
 */
@Service
public class SchemaUpdateService {


    @Autowired
    TableSchemaService tableSchemaService;


    /**
     * Check the differences between two models and update the table schema, if necessary
     * @param oldModel
     * @param newModel
     */
    public void update(Model oldModel, Model newModel) {
        String tableName = newModel.getTable().toLowerCase();

        if (!tableName.equals(oldModel.getTable().toLowerCase())) {
            throw new ModelException("Change table name is not supported: " + oldModel.getTable());
        }

        List<FieldSchema> fields = ModelSchemaComparator.compare(oldModel, newModel);
        if (fields.isEmpty()) {
            return;
        }

        tableSchemaService.updateSchema(newModel.getTable(), fields);
    }
}
