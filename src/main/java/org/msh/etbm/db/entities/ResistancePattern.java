package org.msh.etbm.db.entities;

import org.msh.etbm.db.WorkspaceEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Determine a resistance pattern for DST and Xpert test results
 *  
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="resistancepattern")
public class ResistancePattern extends WorkspaceEntity {

    /**
	 * Criteria to compound the resistance pattern. It may be an exact match or
	 * the match may be part of the pattern in the case
	 * 
	 * @author Ricardo Memoria
	 *
	 */
	public enum PatternCriteria {
		EXACT_RESISTANT, ANY_RESISTANT, EXACT_SUSCEPTIBLE, ANY_SUSCEPTIBLE;
	}

	@Column(length=100, name="PATTERN_NAME")
	private String name;

	@ManyToMany
	@JoinTable(name="substances_resistpattern")
	private List<Substance> substances = new ArrayList<Substance>();

    @Override
    public String getDisplayString() {
        return name;
    }

	/**
	 * The criteria to compound the pattern
	 */
	private PatternCriteria criteria;

	/**
	 * @return the substances
	 */
	public List<Substance> getSubstances() {
		return substances;
	}

	/**
	 * @param substances the substances to set
	 */
	public void setSubstances(List<Substance> substances) {
		this.substances = substances;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @return the criteria
	 */
	public PatternCriteria getCriteria() {
		return criteria;
	}

	/**
	 * @param criteria the criteria to set
	 */
	public void setCriteria(PatternCriteria criteria) {
		this.criteria = criteria;
	}
}
