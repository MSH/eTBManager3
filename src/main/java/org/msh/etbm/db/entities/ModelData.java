package org.msh.etbm.db.entities;

import javax.persistence.*;

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

    @Version
    private int version;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
