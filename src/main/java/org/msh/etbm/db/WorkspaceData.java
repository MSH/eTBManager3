package org.msh.etbm.db;

import org.msh.etbm.commons.Displayable;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.entities.Workspace;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;


/**
 * Super entity class where all entities that support workspace must inherit from.
 * This class contains the workspace information and the transactions that created
 * the entity and update it for the last time
 * 
 * @author Ricardo Memoria
 *
 */
@MappedSuperclass
public abstract class WorkspaceData extends Synchronizable  implements Displayable {

    /**
	 * The workspace of this entity
	 */
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="WORKSPACE_ID")
	@NotNull
	@PropertyLog(ignore=true)
	private Workspace workspace;


	/**
	 * Get the workspace that the entity belongs to
	 * @return
	 */
	public Workspace getWorkspace() {
		return workspace;
	}

	/**
	 * Set the entity workspace
	 * @param workspace
	 */
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}
}
