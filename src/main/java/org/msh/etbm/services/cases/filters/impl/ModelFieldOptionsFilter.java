package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.indicators.keys.Key;
import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.ModelManager;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.sqlquery.QueryDefs;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by rmemoria on 30/8/16.
 */
public class ModelFieldOptionsFilter extends AbstractFilter {

    private String fieldName;
    private String modelName;
    private ModelDAOFactory modelDAOFactory;
    private ModelManager modelManager;
    private List<Item> options;


    public ModelFieldOptionsFilter(String id, String label, String modelName, String fieldName) {
        super(id, label);
        this.modelName = modelName;
        this.fieldName = fieldName;
    }

    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        final List<Item> options = getOptions();
        CompiledModel model = getModelManager().get(modelName);
        Field field = model.getModel().findFieldByName(fieldName);

        String tableName = model.getModel().getTable();
        String fieldName = field.getName();

        if (!tableName.equals(def.getMainTable())) {
            def = def.join(tableName);
            fieldName = tableName + '.' + fieldName;
        }

        addValuesRestriction(def, fieldName, value, item -> {
            // check if item is a valid option
            Item opt = options.stream()
                    .filter(it -> it.getId().equals(item))
                    .findFirst()
                    .get();
            return opt != null ? opt.getId() : null;
        });
    }

    @Override
    public String getFilterType() {
        return "multi-select";
    }

    @Override
    public List<Item> getOptions() {
        if (options == null) {
            ModelDAOFactory factory = getModelDAOFactory();
            ModelDAO dao = factory.create(modelName);
            options = dao.getFieldOptions(fieldName);
        }
        return options;
    }

    /**
     * Get the instance of {@link ModelDAOFactory} from the application context
     * @return instance of {@link ModelDAOFactory}
     */
    private ModelDAOFactory getModelDAOFactory() {
        if (modelDAOFactory == null) {
            modelDAOFactory = getApplicationContext().getBean(ModelDAOFactory.class);
        }

        return modelDAOFactory;
    }

    protected ModelDAO getModelDAO(String modelName) {
        return getModelDAOFactory().create(modelName);
    }

    @Override
    public Map<String, Object> getResources(Map<String, Object> params) {
        ModelDAO dao = getModelDAO(modelName);
        List<Item> options = dao.getFieldOptions(fieldName);

        return options != null ? Collections.singletonMap("options", options) : null;
    }

    @Override
    public void prepareVariableQuery(QueryDefs def, int iteration) {
        CompiledModel model = getModelManager().get(modelName);
        Field field = model.getModel().findFieldByName(fieldName);

        String tableName = model.getModel().getTable();
        String fieldName = field.getName();

        if (tableName.equals(def.getMainTable())) {
            def.select(fieldName);
        } else {
            def.join(tableName).select(tableName + "." + fieldName);
        }
    }


    @Override
    public String getKeyDisplay(Key key) {
        if (key.isNull()) {
            return getMessages().get(Messages.UNDEFINED);
        }

        List<Item> options = getOptions();

        Optional<Item> item = options.stream()
                .filter(it -> it.getId().equals(key))
                .findFirst();

        return item.isPresent() ? item.get().getDisplayString() : key.toString();
    }

    /**
     * Return an instance of the model manager
     * @return instance of {@link ModelManager}
     */
    private ModelManager getModelManager() {
        if (modelManager == null) {
            modelManager = getApplicationContext().getBean(ModelManager.class);
        }

        return modelManager;
    }
}
