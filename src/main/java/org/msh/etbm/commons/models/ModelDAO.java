package org.msh.etbm.commons.models;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.db.*;
import org.msh.etbm.commons.models.impl.ModelResources;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Expose CRUD operations to a model defined in the {@link org.msh.etbm.commons.models.data.Model} class.
 * <p/>
 * instances of this classes are not created directly, but by using a {@link ModelDAOFactory}, which is a
 * Spring component that create all the resources necessary for a ModelDAO to work.
 *
 * Created by rmemoria on 8/7/16.
 */
public class ModelDAO {

    private CompiledModel compiledModel;

    private ModelResources resources;

    public ModelDAO(CompiledModel model, ModelResources resources) {
        this.compiledModel = model;
        this.resources = resources;
    }


    /**
     * Load records of the given model
     * @param displaying
     * @param restriction
     * @return
     */
    public List<RecordData> findMany(boolean displaying, String restriction, Map<String, Object> params) {
        SQLQuerySelectionBuilder builder = new SQLQuerySelectionBuilder();
        builder.setDisplaying(displaying);
        SQLQueryInfo res = builder.generate(compiledModel.getModel(), restriction, resources.getWorkspaceId());

        if (params != null) {
            if (res.getParameters() != null) {
                res.getParameters().putAll(params);
            } else {
                res.setParameters(params);
            }
        }

        SQLQueryLoader loader = new SQLQueryLoader();
        return loader.loadData(resources.getDataSource(), res, displaying);
    }

    /**
     * Create a new record of the model
     * @param values the field values of the model
     * @return the model ID
     */
    public ModelDAOResult insert(Map<String, Object> values) {
        ValidationResult validationRes = compiledModel.validate(values, resources);

        // there are errors from validation ?
        if (validationRes.getErrors().hasErrors()) {
            return new ModelDAOResult(null, validationRes.getErrors());
        }

        SQLGeneratorData genres = SQLGenerator.createInsertSQL(compiledModel.getModel(),
                validationRes.getValues(),
                resources.getWorkspaceId());

        NamedParameterJdbcTemplate templ = new NamedParameterJdbcTemplate(resources.getDataSource());
        templ.update(genres.getSql(), genres.getParams());

        byte[] id = (byte[])genres.getParams().get("id");

        return new ModelDAOResult(ObjectUtils.bytesToUUID(id), null);
    }


    /**
     * Search for one single record by its ID
     * @param id the record ID
     * @param displaying if true indicates that data will be used for display or editing
     * @return instance of {@link RecordData} containing record information, or null if record not found
     */
    public RecordData findOne(UUID id, boolean displaying) {
        String tblName = compiledModel.getModel().resolveTableName();
        Map<String, Object> params = Collections.singletonMap("id", ObjectUtils.uuidAsBytes(id));

        List<RecordData> lst = findMany(displaying, tblName + ".id = :id", params);

        return lst.size() == 1 ? lst.get(0) : null;
    }

    /**
     * Update record data using the given ID and new properties
     * @param id
     * @param values
     */
    public ModelDAOResult update(UUID id, Map<String, Object> values) {
        RecordData rec = findOne(id, false);

        if (rec == null) {
            throw new ModelException("Record not found");
        }

        // update information in the current record
        for (Map.Entry<String, Object> entry: values.entrySet()) {
            rec.getValues().put(entry.getKey(), entry.getValue());
        }

        // validate new record data
        ValidationResult validationRes = compiledModel.validate(rec.getValues(), resources);

        // there are errors from validation ?
        if (validationRes.getErrors().hasErrors()) {
            return new ModelDAOResult(null, validationRes.getErrors());
        }

        SQLGeneratorData genres = SQLGenerator.createUpdateSQL(compiledModel.getModel(),
                validationRes.getValues(),
                resources.getWorkspaceId());

        genres.getParams().put("id", ObjectUtils.uuidAsBytes(id));

        NamedParameterJdbcTemplate templ = new NamedParameterJdbcTemplate(resources.getDataSource());
        templ.update(genres.getSql(), genres.getParams());

        return new ModelDAOResult(id, null);
    }


    /**
     * Remove a record from the model by its ID
     * @param id the ID of the record to be deleted
     */
    public void delete(UUID id) {
        Model model = compiledModel.getModel();
        JdbcTemplate template = new JdbcTemplate(resources.getDataSource());
        template.update("delete from " + model.getTable() + " where id = ?", ObjectUtils.uuidAsBytes(id));
    }


    /**
     * Return the options of a given field
     * @param fieldName the name of the field to get the options from
     * @return List of {@link Item} objects, or null if the list is not found
     */
    public List<Item> getFieldOptions(String fieldName) {
        Model model = compiledModel.getModel();
        Field field = model.findFieldByName(fieldName);

        if (field == null) {
            throw new ModelException("Field not found: " + fieldName);
        }

        if (field.getOptions() == null) {
            return null;
        }

        List<Item> options = field.getOptions().getOptionsValues();
        if (options == null) {
            return null;
        }

        return options.stream()
                .map(item -> new Item(item.getId(), resources.getMessages().eval(item.getName())))
                .collect(Collectors.toList());
    }
}
