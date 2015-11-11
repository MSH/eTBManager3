package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="source")
public class Source extends WorkspaceData {

	@PropertyLog(messageKey="form.name", operations={Operation.NEW, Operation.DELETE})
    @Column(length = 100)
    @NotNull
	private String name;

	@PropertyLog(messageKey= "form.shortName", operations={Operation.NEW, Operation.DELETE})
    @NotNull
	private String shortName;

	@Column(length=50)
	@PropertyLog(messageKey="form.customId", operations={Operation.NEW, Operation.DELETE})
	private String customId;

    private boolean active = true;


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

    @Override
    public String getDisplayString() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
