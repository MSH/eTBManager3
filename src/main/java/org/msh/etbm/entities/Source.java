package org.msh.etbm.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="source")
public class Source extends WSObject implements Serializable {
	private static final long serialVersionUID = -8115568635572935159L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;

	@Embedded
	@PropertyLog(messageKey="form.name", operations={Operation.NEW, Operation.DELETE})
	private LocalizedNameComp name = new LocalizedNameComp();

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="name1", column=@Column(name="ABBREV_NAME1")),
		@AttributeOverride(name="name2", column=@Column(name="ABBREV_NAME2"))
	})
	@PropertyLog(messageKey="form.abbrevName", operations={Operation.NEW, Operation.DELETE})
	private LocalizedNameComp abbrevName = new LocalizedNameComp();

	@Column(length=50)
	@PropertyLog(messageKey="global.legacyId", operations={Operation.NEW, Operation.DELETE})
	private String legacyId;

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Source))
			return false;

		Integer objid = ((Source)other).getId();
		
		if ((objid == null) || (id == null))
			return false;
		
		return (objid.equals(id));
	}
	
	@Override
	public String toString() {
		return getAbbrevName().toString() + " - " + getName().toString();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalizedNameComp getName() {
		return name;
	}

	public void setName(LocalizedNameComp name) {
		this.name = name;
	}

	/**
	 * @return the abbrevName
	 */
	public LocalizedNameComp getAbbrevName() {
		if (abbrevName == null)
			abbrevName = new LocalizedNameComp();
		return abbrevName;
	}

	/**
	 * @param abbrevName the abbrevName to set
	 */
	public void setAbbrevName(LocalizedNameComp abbrevName) {
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
