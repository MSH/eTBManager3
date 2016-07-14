package org.msh.etbm.commons.models;

import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.db.*;
import org.msh.etbm.commons.models.impl.ModelResources;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
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
    public List<RecordData> findMany(boolean displaying, String restriction) {
        SQLQueryBuilder builder = new SQLQueryBuilder();
        builder.setDisplaying(displaying);
        SQLQueryInfo res = builder.generate(compiledModel.getModel(), restriction);

        DataLoader loader = new DataLoader();
        return loader.loadData(resources.getDataSource(), res);
    }

    /**
     * Create a new record of the model
     * @param values the field values of the model
     * @return the model ID
     */
    public ModelDAOResult create(Map<String, Object> values) {
        ValidationResult validationRes = compiledModel.validate(values, resources);

        // there are errors from validation ?
        if (validationRes.getErrors().hasErrors()) {
            return new ModelDAOResult(null, validationRes.getErrors());
        }

        SQLGenerator gen = new SQLGenerator();
        SQLGeneratorData genres = gen.createInsertSQL(compiledModel.getModel(), validationRes.getValues(), resources.getWorkspaceId());

        NamedParameterJdbcTemplate templ = new NamedParameterJdbcTemplate(resources.getDataSource());
        templ.update(genres.getSql(), genres.getParams());

        byte[] id = (byte[])genres.getParams().get("id");

        return new ModelDAOResult(ObjectUtils.bytesToUUID(id), null);
    }


    public void update(UUID id, Map<String, Object> values) {

    }


    public void delete(UUID id) {
        Model model = compiledModel.getModel();
        JdbcTemplate template = new JdbcTemplate(resources.getDataSource());
        template.update("delete from " + model.getTable() + " where id = ?", id);
    }

}
