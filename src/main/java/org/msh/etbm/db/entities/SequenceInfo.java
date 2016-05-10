package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.Synchronizable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sequenceinfo")
public class SequenceInfo extends Synchronizable {


	@Column(name = "seq_name", length = 50)
	@NotNull
	private String sequence;
	
	private int number;

	/**
	 * The workspace of this entity
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WORKSPACE_ID")
	@NotNull
	@PropertyLog(ignore = true)
	private Workspace workspace;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace() {
		return workspace;
	}

	/**
	 * @param workspace the workspace to set
	 */
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}
}
