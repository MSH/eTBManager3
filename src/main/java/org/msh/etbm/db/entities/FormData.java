package org.msh.etbm.db.entities;

import org.msh.etbm.db.WorkspaceEntity;

import javax.persistence.*;

/**
 * Created by rmemoria on 20/1/17.
 */
@Entity
@Table(name = "formdata")
public class FormData extends WorkspaceEntity {

    @Column(length = 50)
    private String formId;

    @Column(length = 200)
    private String name;

    @Lob
    private String data;

    @Version
    private Integer version;

    @Override
    public String getDisplayString() {
        return formId + " - " + name;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
