package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.db.enums.MedicineLine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "substance")
public class Substance extends WorkspaceEntity {

    @NotNull
    @PropertyLog(messageKey = "form.name")
    private String name;

    @PropertyLog(messageKey = "form.shortName")
    @NotNull
    private String shortName;

    @NotNull
    private MedicineLine line;

    private boolean active = true;

    @Column(length = 50)
    @PropertyLog(messageKey = "form.customId")
    private String customId;

    public void setLine(MedicineLine line) {
        this.line = line;
    }

    public MedicineLine getLine() {
        return line;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String getDisplayString() {
        return "(" + shortName + ") " + name;
    }
}
