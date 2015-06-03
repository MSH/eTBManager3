package org.msh.etbm.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;

import javax.persistence.*;

/**
 * Represent an age range for the workspace
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name = "agerange")
public class AgeRange extends WSObject {
	private static final long serialVersionUID = -9151429225415780966L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;
	
	@Embedded
	@PropertyLog(messageKey="form.name")
	private LocalizedNameComp name = new LocalizedNameComp();

	private int iniAge;
	private int endAge;
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (iniAge == 0)
			return "<= " + Integer.toString(endAge);
		if ((endAge == 0) || (endAge > 150))
			return ">= " + Integer.toString(iniAge);

		return Integer.toString(iniAge) + " - " + Integer.toString(endAge);
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public LocalizedNameComp getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(LocalizedNameComp name) {
		this.name = name;
	}

	/**
	 * @return the iniAge
	 */
	public int getIniAge() {
		return iniAge;
	}

	/**
	 * @param iniAge the iniAge to set
	 */
	public void setIniAge(int iniAge) {
		this.iniAge = iniAge;
	}

	/**
	 * @param endAge the endAge to set
	 */
	public void setEndAge(int endAge) {
		this.endAge = endAge;
	}

	/**
	 * @return the endAge
	 */
	public int getEndAge() {
		return endAge;
	}
}
