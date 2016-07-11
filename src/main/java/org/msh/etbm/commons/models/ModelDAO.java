package org.msh.etbm.commons.models;

import org.msh.etbm.commons.models.data.Model;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 8/7/16.
 */
public class ModelDAO {

    private Map<String, Object> values;
    private UUID id;

    private boolean displaying;

    private PreparedModel preparedModel;
    private DataSource dataSource;

    public ModelDAO(PreparedModel model, DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setId(UUID id) {
        // is a null ID ?
        if (id == null) {
            // erase it
            values = null;
        } else {
            // load the record
            // TODO load the record
        }
    }

    public UUID getId() {
        return id;
    }

    public void save() {

    }


    public void delete() {
        if (isNew()) {
            throw new ModelException("Cannot delete an entity that doesn't exist");
        }

        Model model = preparedModel.getModel();
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.update("delete from " + model.getTable() + " where id = ?", id);
    }

    /**
     * Return true if it is a new entity, i.e, it doesn't exist in the database
     *
     * @return true if it is a new entity, or false if this entity is managed by the entity manager
     */
    public boolean isNew() {
        return id == null;
    }

}
