package org.msh.etbm.db.entities;

import org.msh.etbm.db.Synchronizable;
import org.msh.etbm.db.WorkspaceEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Store information about a model in the database table
 * Created by rmemoria on 28/11/16.
 */
@Entity
@Table(name = "modeldata")
public class ModelData extends WorkspaceEntity {

    @NotNull
    private String modelId;

    @Lob
    private String jsonData;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }


    @Override
    public String getDisplayString() {
        return modelId;
    }
}
