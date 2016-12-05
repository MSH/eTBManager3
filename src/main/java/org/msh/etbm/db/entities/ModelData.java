package org.msh.etbm.db.entities;

import org.msh.etbm.db.WorkspaceEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Store information about a model in the database table
 * Created by rmemoria on 28/11/16.
 */
@Entity
@Table(name = "modeldata")
public class ModelData {

    @Id
    private String id;

    @Lob
    private String jsonData;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
