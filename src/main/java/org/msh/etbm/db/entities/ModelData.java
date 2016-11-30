package org.msh.etbm.db.entities;

import org.msh.etbm.db.Synchronizable;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Store information about a model in the database table
 * Created by rmemoria on 28/11/16.
 */
@Entity
@Table(name = "modeldata")
public class ModelData extends Synchronizable {

    @Lob
    private String jsonData;

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
