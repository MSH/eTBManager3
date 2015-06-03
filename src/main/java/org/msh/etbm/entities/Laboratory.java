package org.msh.etbm.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="laboratory")
public class Laboratory extends WSObject implements Serializable {
	private static final long serialVersionUID = 2940261304434288722L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
	
	@Column(length=20)
	@PropertyLog(messageKey="form.abbrevName")
	private String abbrevName;
	
	@Column(length=100)
	@PropertyLog(messageKey="form.name")
	private String name;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="ADMINUNIT_ID")
	@NotNull
	private AdministrativeUnit adminUnit;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="HEALTHSYSTEM_ID")
	private HealthSystem healthSystem;

	@Column(length=50)
	@PropertyLog(messageKey="global.legacyId")
	private String legacyId;

	@Override
	public String toString() {
		return (getAbbrevName() != null? abbrevName + " - " + name: super.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Laboratory))
			return false;
		Laboratory other = (Laboratory) obj;
		Integer otherId = other.getId();
		if (id == null) {
			if (otherId != null)
				return false;
		} else if (!id.equals(otherId)) 
			return false;
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAbbrevName() {
		return abbrevName;
	}

	public void setAbbrevName(String shortName) {
		this.abbrevName = shortName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param adminUnit the adminUnit to set
	 */
	public void setAdminUnit(AdministrativeUnit adminUnit) {
		this.adminUnit = adminUnit;
	}

	/**
	 * @return the adminUnit
	 */
	public AdministrativeUnit getAdminUnit() {
		return adminUnit;
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

	/**
	 * @return the healthSystem
	 */
	public HealthSystem getHealthSystem() {
		return healthSystem;
	}

	/**
	 * @param healthSystem the healthSystem to set
	 */
	public void setHealthSystem(HealthSystem healthSystem) {
		this.healthSystem = healthSystem;
	}
}
