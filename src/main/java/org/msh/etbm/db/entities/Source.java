package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WorkspaceData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="source")
public class Source extends WorkspaceData {

	@PropertyLog(messageKey="form.name", operations={Operation.NEW, Operation.DELETE})
    @Column(length = 100)
	private String name;

	@PropertyLog(messageKey="form.abbrevName", operations={Operation.NEW, Operation.DELETE})
	private String shortName;

	@Column(length=50)
	@PropertyLog(messageKey="global.legacyId", operations={Operation.NEW, Operation.DELETE})
	private String customId;

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
}
