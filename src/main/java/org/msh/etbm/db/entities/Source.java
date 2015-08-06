package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WSObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name="source")
public class Source extends WSObject implements Serializable {
	private static final long serialVersionUID = -8115568635572935159L;

	@PropertyLog(messageKey="form.name", operations={Operation.NEW, Operation.DELETE})
	private String name;

	@PropertyLog(messageKey="form.abbrevName", operations={Operation.NEW, Operation.DELETE})
	private String abbrevName;

	@Column(length=50)
	@PropertyLog(messageKey="global.legacyId", operations={Operation.NEW, Operation.DELETE})
	private String legacyId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the abbrevName
	 */
	public String getAbbrevName() {
		return abbrevName;
	}

	/**
	 * @param abbrevName the abbrevName to set
	 */
	public void setAbbrevName(String abbrevName) {
		this.abbrevName = abbrevName;
	}

	/**
	 * @return the legacyId
	 */
	public String getLegacyId() {
		return legacyId;
	}

	/**
	 * @param legacyId the legacyId to set
	 */
	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}
}
