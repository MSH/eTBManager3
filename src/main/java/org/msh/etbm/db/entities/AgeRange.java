package org.msh.etbm.db.entities;

import org.msh.etbm.db.WorkspaceEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Represent an age range for the workspace
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name = "agerange")
public class AgeRange extends WorkspaceEntity {
	private static final long serialVersionUID = -9151429225415780966L;

	private int iniAge;
	private int endAge;
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (iniAge == 0) {
            return "<= " + Integer.toString(endAge);
        }

		if ((endAge == 0) || (endAge > 150)) {
            return ">= " + Integer.toString(iniAge);
        }

		return Integer.toString(iniAge) + " - " + Integer.toString(endAge);
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

    public String getName() {
        return toString();
    }

    @Override
    public String getDisplayString() {
        return getName();
    }
}
