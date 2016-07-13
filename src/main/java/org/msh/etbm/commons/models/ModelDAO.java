package org.msh.etbm.commons.models;

import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.db.*;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 8/7/16.
 */
public class ModelDAO {

    private CompiledModel compiledModel;
    private DataSource dataSource;
    private UUID workspaceId;

    public ModelDAO(CompiledModel model, DataSource dataSource, UUID workspaceId) {
        this.compiledModel = model;
        this.dataSource = dataSource;
        this.workspaceId = workspaceId;
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
        return loader.loadData(dataSource, res);
    }

    /**
     * Create a new record of the model
     * @param values the field values of the model
     * @return the model ID
     */
    public ModelDAOResult create(Map<String, Object> values) {
        ValidationResult validationRes = compiledModel.validate(values);

        // there are errors from validation ?
        if (validationRes.getErrors().hasErrors()) {
            return new ModelDAOResult(null, validationRes.getErrors());
        }

        SQLGenerator gen = new SQLGenerator();
        SQLGeneratorData genres = gen.createInsertSQL(compiledModel.getModel(), validationRes.getValues(), workspaceId);

        NamedParameterJdbcTemplate templ = new NamedParameterJdbcTemplate(dataSource);
        templ.update(genres.getSql(), genres.getParams());

        byte[] id = (byte[])genres.getParams().get("id");

        return new ModelDAOResult(ObjectUtils.bytesToUUID(id), null);
    }


    public void update(UUID id, Map<String, Object> values) {

    }


    public void delete(UUID id) {
        Model model = compiledModel.getModel();
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update("delete from " + model.getTable() + " where id = ?", id);
    }

}
