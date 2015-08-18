package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name="sequenceinfo")
public class SequenceInfo {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private UUID id;

	@Column(name="seq_name", length=50)
	@NotNull
	private String sequence;
	
	private int number;

	/**
	 * The workspace of this entity
	 */
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="WORKSPACE_ID")
	@NotNull
	@PropertyLog(ignore=true)
	private Workspace workspace;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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
